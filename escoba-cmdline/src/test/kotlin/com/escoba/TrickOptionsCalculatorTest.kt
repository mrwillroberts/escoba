package com.escoba

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItem
import org.junit.Assert.assertThat
import org.junit.Test

class TrickOptionsCalculatorTest {

    val ONE = CardDto("Coin", 1)
    val FIVE = CardDto("Coin", 5)
    val NINE = CardDto("Coin", 9)
    val TEN = CardDto("Coin", 10)

    @Test
    fun noTableCardsReturnsOnlySingleCard() {
        val trickOptions =
                calculateTrickOptions(FIVE, listOf())
        assertThat(trickOptions, hasItem(setOf(FIVE)))
    }

    @Test
    fun tableCardThatMakesTrickReturns() {
        val trickOptions =
                calculateTrickOptions(FIVE, listOf(TEN))
        assertThat(trickOptions, hasItem(setOf(FIVE, TEN)))
    }

    @Test
    fun tableCardThatDoesntMakeTrickReturnsJustChosenCard() {
        val trickOptions =
                calculateTrickOptions(FIVE, listOf(NINE))
        assertThat(trickOptions, equalTo(listOf(setOf(FIVE))))
    }

    @Test
    fun multiCardTrickIsReturned() {
        val trickOptions =
                calculateTrickOptions(FIVE, listOf(NINE, ONE))
        assertThat(trickOptions, hasItem(setOf(FIVE, NINE, ONE)))
    }
}