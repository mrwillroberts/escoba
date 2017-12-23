package com.escoba

import java.util.stream.IntStream

class Game(val deck: Deck, val players: List<Player>) {
    var table = emptyList<Card>()
    var currentPlayer = players[0]

    fun playTurn(cardToPlay: Card) {
        currentPlayer.hand = currentPlayer.hand.filter { h -> h != cardToPlay }
        table += listOf(cardToPlay)

        println("${currentPlayer.name} plays $cardToPlay")

        endOfTurn()
    }

    private fun endOfTurn() {
        currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
        if (isLive() && currentPlayer.hand.isEmpty()) {
            dealRound()
        }
    }

    fun start() {
        dealRound()
        println("Dealing 4 table cards")
        table = deck.draw(4)
    }

    private fun dealRound() {
        println("Dealing each player 3 more cards")
        IntStream.range(0, 3).forEach {
            players.forEach { p ->
                p.hand = p.hand + deck.draw(1)
            }
        }
    }


    fun isLive(): Boolean {
        if (deck.isFull()){
            return false
        }
        return deck.isNotEmpty() || players.count { p -> p.hand.isNotEmpty() } > 0
    }
}

fun createGame(seed:Long = 0): Game {
    val deck = createDeck(seed)
    val players = listOf(
            Player("Player 1"),
            Player("Player 2")
//            Player("Player 3"),
//            Player("Player 4")
    )
    val g = Game(deck, players)
    g.start()
    return g
}

class Player(val name: String) {
    var hand = emptyList<Card>()
    var tricks = emptyList<Trick>()

    override fun toString(): String {
        return "${name}:" +
                "\n\t" + hand.joinToString(",") + " in hand" +
                "\n\t${tricks.size} tricks won"
    }
}