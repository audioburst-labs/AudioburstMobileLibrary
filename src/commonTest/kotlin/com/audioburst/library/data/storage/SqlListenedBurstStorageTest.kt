package com.audioburst.library.data.storage

import com.audioburst.library.interactors.listenedBurstOf
import com.audioburst.library.models.DateTime
import com.audioburst.library.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class SqlListenedBurstStorageTest : DatabaseTest() {

    private val storage by lazy {
        SqlListenedBurstStorage(
            queryRunner = queryRunnerOf(),
            listenedBurstQueries = listenedBurstQueries,
        )
    }

    @Test
    fun testIfInsertIsWorking() = runTest {
        // GIVEN
        val listenedBurst = listenedBurstOf(id = "id")

        // WHEN
        storage.addOrUpdate(listenedBurst)

        // THEN
        assertTrue(storage.getRecentlyListened().isNotEmpty())
    }

    @Test
    fun testIfUpdateIsWorking() = runTest {
        // GIVEN
        val id = "id"
        val firstDate = DateTime.now()
        val secondDate = firstDate.minusDays(3)

        val firstListenedBurst = listenedBurstOf(id = id, dateTime = firstDate)
        val secondListenedBurst = listenedBurstOf(id = id, dateTime = secondDate)

        // WHEN
        storage.addOrUpdate(firstListenedBurst)
        storage.addOrUpdate(secondListenedBurst)

        // THEN
        assertTrue(storage.getRecentlyListened().isNotEmpty())
        assertTrue(storage.getRecentlyListened().size == 1)
        assertEquals(secondDate, storage.getRecentlyListened().first().dateTime)
    }

    @Test
    fun testIfListenedBurstsOnlyFromLast30DaysAreGettingSelected() = runTest {
        // GIVEN
        val now = DateTime.now()
        val listenedBurstsDaysAgo = listOf(0, 5, 10, 20, 30, 31, 32, 35)
        val listenedBursts = listenedBurstsDaysAgo.mapIndexed { index, i ->
            listenedBurstOf(
                id = "id$index",
                dateTime = now.minusDays(i.toLong()),
            )
        }

        // WHEN
        listenedBursts.forEach { storage.addOrUpdate(it) }
        val all = storage.getRecentlyListened()

        // THEN
        assertEquals(listenedBurstsDaysAgo.filter { it <= 30 }.size, all.size)
    }

    @Test
    fun testIfListenedBurstsOlderThan30DaysAreGettingDeleted() = runTest {
        // GIVEN
        val now = DateTime.now()
        val listenedBurstsDaysAgo = listOf(0, 5, 10, 20, 30, 31, 32, 35)
        val listenedBursts = listenedBurstsDaysAgo.mapIndexed { index, i ->
            listenedBurstOf(
                id = "id$index",
                dateTime = now.minusDays(i.toLong()),
            )
        }

        // WHEN
        listenedBursts.forEach { storage.addOrUpdate(it) }
        storage.removeExpiredListenedBursts()
        val all = listenedBurstQueries.selectAll().executeAsList()

        // THEN
        assertEquals(listenedBurstsDaysAgo.filter { it <= 30 }.size, all.size)
    }
}