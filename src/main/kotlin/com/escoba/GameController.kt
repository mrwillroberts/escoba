package com.escoba

import org.springframework.web.bind.annotation.*
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

    @PutMapping("/game/{id}")
    fun playCard(@PathVariable(value = "id") id: Long, @RequestBody card: CardDto) {
        gameMap.get(id)?.playTurn(Card(Suit(card.suit),card.value))
    }
}

fun toGameView(id: Long, game: Game) : GameView {
    return GameView(
            id,
            game.isLive(),
            game.currentPlayer.name,
            game.players.map { player -> player.name },
            game.deck.size(),
            game.table.map(::toCardDto),
            game.currentPlayer.hand.map(::toCardDto))
}

private fun toCardDto(card:Card) = CardDto(card.suit.name, card.numericValue)

data class GameView(
        val id: Long,
        val live:Boolean,
        val currentPlayer:String,
        val players:List<String>,
        val cardsInDeck:Int,
        val tableCards:List<CardDto>,
        val currentPlayerHand:List<CardDto>) // TODO move this off

data class CardDto(val suit:String, val value:Int)