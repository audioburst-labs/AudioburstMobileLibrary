package com.audioburst.library.data.storage

import com.audioburst.library.utils.Settings
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsUserStorageTest {

    private val settings = MockSettings()
    private val storage = SettingsUserStorage(
        settingsName = "",
        settings = settings
    )

    @AfterTest
    fun clearSettings() {
        settings.clear()
    }

    @Test
    fun testIfStorageValuesAreNullAfterInitialization() {
        assertEquals(expected = null, storage.userId)
    }

    @Test
    fun testIfUserIdIsSavedProperly() {
        // GIVEN
        val userId = "userId"

        // WHEN
        storage.userId = userId

        // THEN
        assertEquals(userId, storage.userId)
    }

    @Test
    fun testIfUserIdIsNulledOut() {
        // GIVEN
        val userId = null

        // WHEN
        storage.userId = userId

        // THEN
        assertEquals(userId, storage.userId)
    }
}

private class MockSettings: Settings {

    private val map = mutableMapOf<String, String?>()

    override fun getStringOrNull(key: String): String? = map[key]

    override fun putString(key: String, value: String?) {
        map[key] = value
    }

    fun clear() {
        map.clear()
    }
}