package com.escoba

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test


class GameTest {
    private val player1 = Player("A")
    private val player2 = Player("B")

    val g = Game(createDeck(), listOf(player1, player2))

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


    @Test(expected = Exception::class)
    fun cantCreateGameWithoutPlayers() {
        Game(Deck(listOf()), listOf())
    }
}