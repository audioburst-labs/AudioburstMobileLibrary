package com.audioburst.library.interactors

import com.audioburst.library.data.repository.mappers.InMemoryUserStorage
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class SetFilterListenedBurstsTest {

    private val userStorage = InMemoryUserStorage()
    private val interactor = SetFilterListenedBursts(userStorage = userStorage)

    @AfterTest
    fun clear() {
        userStorage.clear()
    }

    @Test
    fun testIfEnablingFilterListenedBurstsResultsInTrueInUserStorage() {
        // GIVEN
        val filterListenedBursts = true

        // WHEN
        interactor(filterListenedBursts)

        // THEN
        assertEquals(filterListenedBursts, userStorage.filterListenedBursts)
    }

    @Test
    fun testIfDisablingFilterListenedBurstsResultsInFalseInUserStorage() {
        // GIVEN
        val filterListenedBursts = false

        // WHEN
        interactor(filterListenedBursts)

        // THEN
        assertEquals(filterListenedBursts, userStorage.filterListenedBursts)
    }
}