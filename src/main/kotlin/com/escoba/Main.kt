package com.escoba

import java.util.*
import java.util.stream.IntStream.range
import kotlin.streams.toList

fun main(args: Array<String>) {

    val suits = listOf("Coin", "Cup", "Sword", "Club").map { s -> Suit(s) }
    val cards = suits.map { suit -> range(1, 11).mapToObj { n -> Card(suit, n) }.toList() }.flatten()
    val deck = Deck(cards.shuffle(seed = 0))
    val players = listOf(
            Player("Player 1"),
            Player("Player 2")
//            Player("Player 3"),
//            Player("Player 4")
    )
    val g = Game(deck, players)
    val renderer = ConsoleRenderer()

    g.start()

    while (g.isLive()) {
        val chosenCard = renderer.chooseCard(g)
        g.playTurn(chosenCard)
    }

    renderer.showState(g)
}

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

class ConsoleRenderer {
    fun showState(g: Game) {
        println("Table: " + g.table.joinToString(" "))
        println()
        println("Current Player: ${g.currentPlayer.name}")
        println()
        g.players.forEach(::println)
        println()
        println("(${g.deck.size()} cards left in deck)")
    }

    fun chooseCard(g: Game): Card {

        val hand = g.currentPlayer.hand

        println("\nTable: " + g.table.joinToString(" "))
        var choice: Int? = null
        while (!isValidChoice(choice, hand.size)) {
            println("Choices:")
            range(0, hand.size)
                .mapToObj { i -> "\t[${i}] ${hand[i]}" }
                .forEach(::println)
            print("${g.currentPlayer.name} choose a card:")
            choice = readLine()?.toIntOrNull()
        }
        return hand[checkNotNull(choice)]
    }

    fun isValidChoice(choice: Int?, noOfChoices: Int): Boolean {
        return choice != null && choice >= 0 && choice < noOfChoices
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
        range(0, 3).forEach {
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