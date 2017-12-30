package com.escoba

import java.util.stream.IntStream

class Game(val deck: Deck, val players: List<Player>) {
    var table = emptyList<Card>()
    var currentPlayer = players[0]

    fun start() {
        dealRound()
        println("Dealing 4 table cards")
        table = deck.draw(4)
    }

    fun playTurn(cardToPlay: Card, tableCardsToPlay: Set<Card> = setOf()) {
        validateArguments(cardToPlay)

        currentPlayer.hand = currentPlayer.hand.filter { h -> h != cardToPlay }

        if (tableCardsToPlay.isEmpty()){
            println("${currentPlayer.name} plays $cardToPlay")
            table += listOf(cardToPlay)
        } else {
            val trick = Trick(tableCardsToPlay + cardToPlay)
            println("${currentPlayer.name} plays $trick")

            table = table.filter { c->!tableCardsToPlay.contains(c) }
            currentPlayer.tricks += trick
        }


        endOfTurn()
    }

    private fun validateArguments(cardToPlay: Card) {
        if (!currentPlayer.hand.contains(cardToPlay)) {
            throw IllegalArgumentException("${currentPlayer.name} does not have ${cardToPlay})}")
        }
    }

    fun isLive(): Boolean {
        if (deck.isFull()){
            return false
        }
        return deck.isNotEmpty() || players.count { p -> p.hand.isNotEmpty() } > 0
    }

    private fun endOfTurn() {
        currentPlayer = players[(players.indexOf(currentPlayer) + 1) % players.size]
        if (isLive() && currentPlayer.hand.isEmpty()) {
            dealRound()
        }
    }


    private fun dealRound() {
        println("Dealing each player 3 more cards")
        IntStream.range(0, 3).forEach {
            players.forEach { p ->
                p.hand = p.hand + deck.draw(1)
            }
        }
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