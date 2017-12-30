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

enum class Suit {
    Coin, Cup, Sword, Club
}

data class Card(var suit: Suit, var numericValue: Int) {
    override fun toString(): String {
        return "<${suit.name} ${numericValue}>"
    }
}

data class Trick(val cards: Collection<Card>){
    init {
        val trickValue = cards.map { card -> card.numericValue }.sum()
        if (trickValue != 15) {
            throw IllegalArgumentException("Trick ${cards} value ${trickValue}!=15")
        }
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

fun createDeck(seed: Long? = null): Deck {
    val cards = cards()
    val deck = Deck(cards.shuffle(seed))
    return deck
}

fun cards(): List<Card> {
    val suits = Suit.values()
    val cards = suits.map { suit -> IntStream.range(1, 11).mapToObj { n -> Card(suit, n) }.toList() }.flatten()
    return cards
}