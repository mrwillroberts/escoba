package com.escoba

import org.springframework.web.bind.annotation.*

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
    fun playTurn(@PathVariable(value = "id") id: Long, @RequestBody turn: TurnDto) {
        gameRepository.load(id)?.playTurn(toCard(turn.chosenCard), turn.tableCards.map(::toCard).toSet())
    }

}

private fun toCard(cardDto: CardDto) = Card(Suit.valueOf(cardDto.suit), cardDto.value)

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