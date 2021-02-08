package com.audioburst.library.models

import com.audioburst.library.data.repository.mappers.keyOf
import com.audioburst.library.data.repository.mappers.preferenceOf
import com.audioburst.library.data.repository.mappers.userPreferenceOf
import kotlin.test.Test
import kotlin.test.assertEquals

class UserPreferenceTest {

    @Test
    fun testIfUpdatePreferenceWorksAsExpected() {
        // GIVEN
        val userPreferences = userPreferenceOf()
        val newPreferences = listOf(
            preferenceOf(
                name = "name",
                source = "source",
                take = 1,
                offer = 1,
                keys = listOf(keyOf()),
            )
        )

        // WHEN
        val updatedUserPreferences = userPreferences.updatePreference(preferences = newPreferences)

        // THEN
        assertEquals(userPreferences.id, updatedUserPreferences.id)
        assertEquals(userPreferences.userId, updatedUserPreferences.userId)
        assertEquals(userPreferences.location, updatedUserPreferences.location)
        assertEquals(userPreferences.sourceType, updatedUserPreferences.sourceType)
        assertEquals(newPreferences, updatedUserPreferences.preferences)
    }
}