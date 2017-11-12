package com.escoba

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@RestController
class GameController {

    val counter = AtomicLong()
    val gameMap = ConcurrentHashMap<Long, Game>()

    @GetMapping("/game/create")
    fun createGame(@RequestParam(value = "name", defaultValue = "World") name: String) : Long {
        val gameId = counter.incrementAndGet()
        gameMap.putIfAbsent(gameId, createGame())
        return gameId
    }

    @GetMapping("/game/{id}")
    fun getGame(@PathVariable(value = "id") id: Long) : GameView? {
        return gameMap.get(id)?.let{ game -> toGameView(id,game) }
    }
}

fun toGameView(id: Long, game: Game) : GameView {
    return GameView(
            id,
            game.currentPlayer.name,
            game.players.map { player -> player.name },
            game.deck.size(),
            game.table.map { card -> CardDto(card.suit.name, card.numericValue) })
}

data class GameView(
        val id: Long,
        val currentPlayer:String,
        val players:List<String>,
        val cardsInDeck:Int,
        val tableCards:List<CardDto>)

data class CardDto(val suit:String, val value:Int)