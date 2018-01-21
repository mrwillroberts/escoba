package com.escoba


fun calculateTrickOptions(chosenCard: CardDto, tableCards: List<CardDto>): List<Set<CardDto>> {
    return listOf(setOf(chosenCard)) + recCalcTrickOptions(setOf(chosenCard), tableCards)
}

private fun recCalcTrickOptions(chosenCards: Set<CardDto>, tableCards: List<CardDto>) : List<Set<CardDto>> {
    val options = mutableListOf<Set<CardDto>>()
    if (chosenCards.sumBy { c -> c.value } == 15) {
        options.add(chosenCards)
    }

    var i = 0
    for (tableCard in tableCards) {
        i++
        val combination = chosenCards + tableCard

        if (combination.sumBy { c -> c.value } <= 15) {

            options += recCalcTrickOptions(combination, tableCards.drop(i))
        }
    }
    return options.toList()
}