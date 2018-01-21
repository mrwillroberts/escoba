package com.escoba

data class GameView(
        val id: Long,
        val live:Boolean,
        val currentPlayer:String,
        val players:List<String>,
        val cardsInDeck:Int,
        val tableCards:List<CardDto>,
        val currentPlayerHand:List<CardDto>) // TODO move this off

data class CardDto(val suit:String, val value:Int) {
    override fun toString(): String {
        return "<${suit} ${value}>"
    }
}

data class TurnDto(val chosenCard:CardDto, val tableCards:List<CardDto>)