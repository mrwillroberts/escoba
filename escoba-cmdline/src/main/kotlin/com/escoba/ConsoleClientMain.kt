package com.escoba

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.DefaultUriTemplateHandler
import java.util.stream.IntStream.range
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter



fun main(args: Array<String>) {
    val api = restClient()

    val gameId = api.getForEntity("/game/create",Long::class.java).body
    var gameView = api.getForEntity("/game/${gameId}",GameView::class.java).body

    println(gameView)

    val renderer = ConsoleRenderer()

    while (gameView.live) {
        val chosenCard = renderer.chooseCard(gameView,gameView.currentPlayerHand)
        val tableCards = renderer.chooseTableCards(chosenCard, gameView.tableCards)
        api.put("/game/${gameId}", TurnDto(chosenCard,tableCards.toList()))

        gameView = api.getForEntity("/game/${gameId}",GameView::class.java).body
    }

    renderer.showState(gameView)
}

private fun restClient(): RestTemplate {
    val api = RestTemplate()

    val defaultUriTemplateHandler = DefaultUriTemplateHandler()
    defaultUriTemplateHandler.baseUrl = "http://localhost:8080/api"
    api.uriTemplateHandler = defaultUriTemplateHandler

    val mappingJackson2HttpMessageConverter = MappingJackson2HttpMessageConverter()
    mappingJackson2HttpMessageConverter.objectMapper = ObjectMapper().registerKotlinModule()
    api.messageConverters.add(mappingJackson2HttpMessageConverter)
    return api
}

class ConsoleRenderer {
    fun showState(g: GameView) {
        println("Table: " + g.tableCards.joinToString(" "))
        println()
        println("Current Player: ${g.currentPlayer}")
        println()
        g.players.forEach(::println)
        println()
        println("(${g.cardsInDeck} cards left in deck)")
    }

    fun chooseCard(g: GameView, hand: List<CardDto>): CardDto {

        println("\nTable: " + g.tableCards.joinToString(" "))
        var choice: Int? = null
        while (!isValidChoice(choice, hand.size)) {
            println("Choices:")
            range(0, hand.size)
                .mapToObj { i -> "\t[${i}] ${hand[i]}" }
                .forEach(::println)
            print("${g.currentPlayer} choose a card:")
            choice = readLine()?.toIntOrNull()
        }
        return hand[checkNotNull(choice)]
    }

    fun isValidChoice(choice: Int?, noOfChoices: Int): Boolean {
        return choice != null && choice >= 0 && choice < noOfChoices
    }

    fun chooseTableCards(chosenCard: CardDto, tableCards: List<CardDto>): Set<CardDto> {
        val trickOptions = calculateTrickOptions(chosenCard, tableCards)

        var choice: Int? = null
        while (!isValidChoice(choice, trickOptions.size)) {
            println("Options:")

            range(0, trickOptions.size)
                    .mapToObj { i -> "\t[${i}] ${trickOptions[i]}"}
                    .forEach(::println)

            print("Choose an option:")
            choice = readLine()?.toIntOrNull()
        }

        return trickOptions[checkNotNull(choice)]-chosenCard

    }

}

