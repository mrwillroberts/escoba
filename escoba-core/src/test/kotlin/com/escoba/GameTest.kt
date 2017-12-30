package com.escoba

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test


class GameTest {
    private val player1 = Player("A")
    private val player2 = Player("B")

    val deck = createDeck()
    var g = Game(deck, listOf(player1, player2))

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
        deck.draw(38) // remove most cards
        g.start()

        assertThat(g.isLive(), equalTo(true))

        g.playTurn(player1.hand[0])
        g.playTurn(player2.hand[0])

        assertThat(g.isLive(), equalTo(false))

    }

    @Test
    fun turnWithTableCardsAddsTrickToCurrentPlayer() {

        player1.hand += Coin.FIVE
        g.table += Coin.TEN

        assertThat(player1.tricks.isEmpty(), equalTo(true))

        g.playTurn(Coin.FIVE, setOf(Coin.TEN))

        assertThat(player1.tricks.isEmpty(), equalTo(false))
        assertThat(player2.tricks.isEmpty(), equalTo(true))
    }

    @Test(expected = Exception::class)
    fun playerCantPlayIllegalTableCard() {
        player1.hand += Coin.FIVE

        g.playTurn(Coin.FIVE, setOf(Coin.TEN))
    }

    @Test(expected = Exception::class)
    fun sumOfCardsPlayedNotEqualTo15IsIllegal() {
        player1.hand += Coin.FIVE
        g.table += Coin.NINE

        g.playTurn(Coin.FIVE, setOf(Coin.NINE))
    }

    @Test(expected = Exception::class)
    fun playerCantPlayIllegalHandCard() {
        gameIsNotLiveUntilStarted()

        g.playTurn(player2.hand[0]) // player 1 shouldn't be able to play player 2's cards
    }


    @Test(expected = Exception::class)
    fun cantCreateGameWithoutPlayers() {
        Game(Deck(listOf()), listOf())
    }
}