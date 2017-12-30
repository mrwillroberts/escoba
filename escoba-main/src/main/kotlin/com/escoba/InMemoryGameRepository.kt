package com.escoba

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class InMemoryGameRepository {

    val counter = AtomicLong()
    val gameMap = ConcurrentHashMap<Long, Game>()

    fun save(game:Game, id:Long?=null) : Long {
        val gameId = id ?: counter.incrementAndGet()
        gameMap.putIfAbsent(gameId, game)
        return gameId
    }

    fun load(id:Long) : Game? {
        return gameMap.get(id)
    }
}