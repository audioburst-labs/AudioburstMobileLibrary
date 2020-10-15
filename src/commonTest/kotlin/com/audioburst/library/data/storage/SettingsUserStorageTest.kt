package com.audioburst.library.data.storage

import com.russhwolf.settings.MockSettings
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsUserStorageTest {

    private val settings = MockSettings()
    private val storage = SettingsUserStorage(
        settings = settings
    )

    @AfterTest
    fun clearSettings() {
        settings.clear()
    }

    @Test
    fun testIfStorageValuesAreNullAfterInitialization() {
        assertEquals(expected = null, storage.userId)
        assertEquals(expected = null, storage.deviceId)
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
    fun testIfDeviceIdIsSavedProperly() {
        // GIVEN
        val deviceId = "deviceId"

        // WHEN
        storage.deviceId = deviceId

        // THEN
        assertEquals(deviceId, storage.deviceId)
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
    fun testIfDeviceIdIsNulledOut() {
        // GIVEN
        val deviceId = null

        // WHEN
        storage.deviceId = deviceId

        // THEN
        assertEquals(deviceId, storage.deviceId)
    }
}