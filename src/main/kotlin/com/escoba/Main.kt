package com.escoba

import java.util.stream.IntStream.range

fun main(args: Array<String>) {

    val g = createGame()

    val renderer = ConsoleRenderer()

    while (g.isLive()) {
        val chosenCard = renderer.chooseCard(g)
        g.playTurn(chosenCard)
    }

    renderer.showState(g)
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

