import java.util.*
import java.util.stream.IntStream.range
import kotlin.streams.toList

fun main(args: Array<String>) {

    val suits = listOf("Coin","Cup","Sword","Club").map { s -> Suit(s) }
    val cards = suits.map { suit -> range(1,11).mapToObj{n->Card(suit, n)}.toList() }.flatten()
    cards.shuffle().forEach(::println)
}

data class Suit(var name:String)
data class Card(var suit:Suit, var numericValue: Int)

/**
 * Returns a randomized list.
 */
fun <T> Iterable<T>.shuffle(seed: Long? = null): List<T> {
    val list = this.toMutableList()
    val random = if (seed != null) Random(seed) else Random()
    Collections.shuffle(list, random)
    return list
}