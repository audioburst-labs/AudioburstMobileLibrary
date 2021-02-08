package com.audioburst.library.models

import com.audioburst.library.data.repository.mappers.keyOf
import kotlin.test.Test
import kotlin.test.assertEquals

class KeyTest {

    @Test
    fun testIfUpdatePreferenceWorksAsExpected() {
        // GIVEN
        val selected = true
        val key = keyOf(selected = !selected)

        // WHEN
        val updatedKey = key.updateSelected(selected = selected)

        // THEN
        assertEquals(key.key, updatedKey.key)
        assertEquals(key.segCategory, updatedKey.segCategory)
        assertEquals(key.source, updatedKey.source)
        assertEquals(key.sourceId, updatedKey.sourceId)
        assertEquals(key.position, updatedKey.position)
        assertEquals(selected, updatedKey.selected)
    }
}