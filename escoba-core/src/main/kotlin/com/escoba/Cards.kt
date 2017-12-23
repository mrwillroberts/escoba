package com.escoba

import java.util.*

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

/**
 * Returns a randomized list.
 */
fun <T> Iterable<T>.shuffle(seed: Long? = null): List<T> {
    val list = this.toMutableList()
    val random = if (seed != null) Random(seed) else Random()
    Collections.shuffle(list, random)
    return list
}

