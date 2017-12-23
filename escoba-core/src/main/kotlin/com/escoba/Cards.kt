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
    fun isFull(): Boolean = cards.size == 40
}

data class Suit(var name: String)
data class Card(var suit: Suit, var numericValue: Int) {
    override fun toString(): String {
        return "<${suit.name} ${numericValue}>"
    }
}

data class Trick(val cards: Collection<Card>)

/**
 * Returns a randomized list.
 */
fun <T> Iterable<T>.shuffle(seed: Long? = null): List<T> {
    val list = this.toMutableList()
    val random = if (seed != null) Random(seed) else Random()
    Collections.shuffle(list, random)
    return list
}

fun createDeck(seed: Long? = null): Deck {
    val suits = listOf("Coin", "Cup", "Sword", "Club").map { s -> Suit(s) }
    val cards = suits.map { suit -> IntStream.range(1, 11).mapToObj { n -> Card(suit, n) }.toList() }.flatten()
    val deck = Deck(cards.shuffle(seed))
    return deck
}