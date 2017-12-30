package com.escoba

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test


class GameTest {
    private val player1 = Player("A")
    private val player2 = Player("B")

    var g = Game(createDeck(), listOf(player1, player2))

    @Test
    fun gameIsNotLiveUntilStarted() {

        assertThat(g.isLive(), equalTo(false))
        g.start()
        assertThat(g.isLive(), equalTo(true))
    }

    @Test
    fun initialCurrentPlayerIsFirstPlayerInList() {
        gameIsNotLiveUntilStarted()

        assertThat(g.currentPlayer, equalTo(player1))
    }

    @Test
    fun currentPlayerChangesAfterTurnPlayed() {
        initialCurrentPlayerIsFirstPlayerInList()

        g.playTurn(player1.hand[0])

        assertThat(g.currentPlayer, equalTo(player2))
    }

    @Test
    fun playReturnsToFirstAfterAllOtherPlayers() {
        currentPlayerChangesAfterTurnPlayed()

        g.playTurn(player2.hand[0])

        assertThat(g.currentPlayer, equalTo(player1))
    }

    @Test
    fun onceAllCardsArePlayedGameIsNoLongerLive() {
        g = Game(Deck(cards().take(2)), listOf(player1,player2))
        g.start()

        assertThat(g.isLive(), equalTo(true))

        g.playTurn(player1.hand[0])
        g.playTurn(player2.hand[0])

        assertThat(g.isLive(), equalTo(false))

    }

    @Test
    fun turnWithTableCardsAddsTrickToCurrentPlayer() {
        val fiveOfCoins = Card(Suit("Coin"), 5)
        val tenOfCoins = Card(Suit("Coin"), 10)

        g = Game(Deck(listOf()), listOf(player1, player2))
        player1.hand += fiveOfCoins
        g.table += tenOfCoins

        assertThat(player1.tricks.isEmpty(), equalTo(true))

        g.playTurn(fiveOfCoins, setOf(tenOfCoins))

        assertThat(player1.tricks.isEmpty(), equalTo(false))
        assertThat(player2.tricks.isEmpty(), equalTo(true))

    }

    @Test(expected = Exception::class)
    fun playerCantPlayIllegalCard() {
        gameIsNotLiveUntilStarted()

        g.playTurn(player2.hand[0]) // player ones shouldn't be able to play player 2's cards
    }


    @Test(expected = Exception::class)
    fun cantCreateGameWithoutPlayers() {
        Game(Deck(listOf()), listOf())
    }
}