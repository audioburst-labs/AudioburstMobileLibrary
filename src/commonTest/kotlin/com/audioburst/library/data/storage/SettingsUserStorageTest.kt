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
    fun testIfSelectedKeysCountIsEqualZeroAfterInitialization() {
        assertEquals(expected = 0, storage.selectedKeysCount)
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

    @Test
    fun testIfSelectedKeysCountIsSavedProperly() {
        // GIVEN
        val selectedKeysCount = 5

        // WHEN
        storage.selectedKeysCount = selectedKeysCount

        // THEN
        assertEquals(selectedKeysCount, storage.selectedKeysCount)
    }

    @Test
    fun testIfFilterListenedBurstsInitialValueIsTrue() {
        // GIVEN
        val expectedInitialValue = true

        // WHEN

        // THEN
        assertEquals(expectedInitialValue, storage.filterListenedBursts)
    }

    @Test
    fun testIfFilterListenedBurstsIsSavedProperly() {
        // GIVEN
        val filterListenedBursts = false

        // WHEN
        storage.filterListenedBursts = filterListenedBursts

        // THEN
        assertEquals(filterListenedBursts, storage.filterListenedBursts)
    }
}

private class MockSettings: Settings {

    private val map = mutableMapOf<String, Any?>()

    override fun getStringOrNull(key: String): String? = map[key] as? String

    override fun putString(key: String, value: String?) {
        map[key] = value
    }

    override fun getIntOrDefault(key: String, default: Int): Int = map[key] as? Int ?: default

    override fun putInt(key: String, value: Int) {
        map[key] = value
    }

    override fun getBooleanOrDefault(key: String, default: Boolean): Boolean = map[key] as? Boolean ?: default

    override fun putBoolean(key: String, value: Boolean) {
        map[key] = value
    }

    fun clear() {
        map.clear()
    }
}