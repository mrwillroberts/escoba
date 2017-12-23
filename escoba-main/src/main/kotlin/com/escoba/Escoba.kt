package com.escoba

import java.util.*
import java.util.stream.IntStream
import kotlin.streams.toList

data class Deck(private var cards: Collection<Card>) {
    fun draw(number: Int): List<Card> {
        val draw = cards.take(number)
        cards = cards.drop(number)
        return draw
    }

    fun size(): Int = cards.size

    fun isNotEmpty(): Boolean = cards.isNotEmpty()
}

data class Suit(var name: String)
data class Card(var suit: Suit, var numericValue: Int) {
    override fun toString(): String {
        return "<${suit.name} ${numericValue}>"
    }
}

data class Trick(val cards: Collection<Card>)
class Player(val name: String) {
    var hand = emptyList<Card>()
    var tricks = emptyList<Trick>()

    override fun toString(): String {
        return "${name}:" +
                "\n\t" + hand.joinToString(",") + " in hand" +
                "\n\t${tricks.size} tricks won"
    }
}

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
        return deck.isNotEmpty() || players.count { p -> p.hand.isNotEmpty() } > 0
    }
}

/**
 * Returns a randomized list.
 */
fun <T> Iterable<T>.shuffle(seed: Long? = null): List<T> {
    val list = this.toMutableList()
    val random = if (seed != null) Random(seed) else Random()
    Collections.shuffle(list, random)
    return list
}

fun createGame(seed:Long = 0): Game {
    val suits = listOf("Coin", "Cup", "Sword", "Club").map { s -> Suit(s) }
    val cards = suits.map { suit -> IntStream.range(1, 11).mapToObj { n -> Card(suit, n) }.toList() }.flatten()
    val deck = Deck(cards.shuffle(seed))
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