package com.audioburst.library.interactors

import com.audioburst.library.data.repository.mappers.InMemoryUserStorage
import com.audioburst.library.data.repository.mappers.keyOf
import com.audioburst.library.data.repository.mappers.preferenceOf
import com.audioburst.library.data.repository.mappers.userPreferenceOf
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.Preference
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class UpdateSelectedKeysCountTest {

    private val userStorage: UserStorage = InMemoryUserStorage()
    private val interactor: UpdateSelectedKeysCount = UpdateSelectedKeysCount(userStorage)

    @AfterTest
    fun clearStorage() {
        userStorage.selectedKeysCount = 0
    }

    private fun preferencesWithSelectedKeys(selectedKeysCount: Int): Preference =
        preferenceOf(
            keys = (0 until selectedKeysCount).map {
                keyOf(selected = true)
            }
        )

    @Test
    fun testIfCorrectSelectedKeyCountIsWrittenToTheStorage() {
        // GIVEN
        val selectedKeysCount = 6
        val userPreferences = userPreferenceOf(
            preferences = listOf(preferencesWithSelectedKeys(selectedKeysCount))
        )

        // WHEN
        interactor(userPreferences)

        // THEN
        assertEquals(selectedKeysCount, userStorage.selectedKeysCount)
    }

    @Test
    fun testIfCorrectSelectedKeyCountIsWrittenToTheStorageWhenMoreThanOnePreferenceOnList() {
        // GIVEN
        val preferencesCount = 3
        val selectedKeysCount = 6
        val userPreferences = userPreferenceOf(
            preferences = (0 until preferencesCount).map { preferencesWithSelectedKeys(selectedKeysCount) }
        )

        // WHEN
        interactor(userPreferences)

        // THEN
        assertEquals(preferencesCount * selectedKeysCount, userStorage.selectedKeysCount)
    }
}