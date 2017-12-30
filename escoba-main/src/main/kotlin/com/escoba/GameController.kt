package com.escoba

import org.springframework.web.bind.annotation.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

@RestController
class GameController {

    val gameRepository = InMemoryGameRepository()

    @GetMapping("/game/create")
    fun createGame() : Long {
        return gameRepository.save(game())
    }

    @GetMapping("/game/{id}")
    fun getGame(@PathVariable(value = "id") id: Long) : GameView? {
        return gameRepository.load(id)?.let{ game -> toGameView(id,game) }
    }

    @PutMapping("/game/{id}")
    fun playCard(@PathVariable(value = "id") id: Long, @RequestBody card: CardDto) {
        gameRepository.load(id)?.playTurn(Card(Suit.valueOf(card.suit),card.value))
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