package com.audioburst.library.models

import com.audioburst.library.data.repository.mappers.keyOf
import com.audioburst.library.data.repository.mappers.preferenceOf
import kotlin.test.Test
import kotlin.test.assertEquals

class PreferenceTest {

    @Test
    fun testIfUpdateKeysWorksAsExpected() {
        // GIVEN
        val preference = preferenceOf()
        val newKeys = listOf(
            keyOf(
                key = "key",
                segCategory = "segCategory",
                source = "source",
                sourceId = 1,
                position = 1,
                selected = true,
            )
        )

        // WHEN
        val updatedPreferences = preference.updateKeys(keys = newKeys)

        // THEN
        assertEquals(preference.name, updatedPreferences.name)
        assertEquals(preference.source, updatedPreferences.source)
        assertEquals(preference.take, updatedPreferences.take)
        assertEquals(preference.offer, updatedPreferences.offer)
        assertEquals(newKeys, updatedPreferences.keys)
    }
}