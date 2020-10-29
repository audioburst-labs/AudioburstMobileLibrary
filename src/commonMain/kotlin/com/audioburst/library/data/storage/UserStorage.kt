package com.audioburst.library.data.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

internal interface UserStorage {

    var userId: String?

    var deviceId: String?
}

internal class SettingsUserStorage(
    private val settings: Settings
) : UserStorage {

    override var userId: String?
        get() = settings.getStringOrNull(USER_ID_KEY)
        set(value) {
            settings[USER_ID_KEY] = value
        }

    override var deviceId: String?
        get() = settings.getStringOrNull(DEVICE_ID_KEY)
        set(value) {
            settings[DEVICE_ID_KEY] = value
        }
}

private const val SETTINGS_NAME = "com.audioburst.library"
private const val USER_ID_KEY = "$SETTINGS_NAME.userId"
private const val DEVICE_ID_KEY = "$SETTINGS_NAME.deviceId"

internal fun settings(): Settings = createSettings(SETTINGS_NAME)

internal expect fun createSettings(name: String): Settings
