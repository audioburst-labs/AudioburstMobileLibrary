package com.audioburst.library.data.storage

import com.audioburst.library.utils.Settings

internal interface UserStorage {

    var userId: String?

    var selectedKeysCount: Int
}

internal class SettingsUserStorage(
    settingsName: String,
    private val settings: Settings
) : UserStorage {

    private val userIdKey = "$settingsName.userId"
    private val selectedKeysCountKey = "$settingsName.selectedKeysCount"

    override var userId: String?
        get() = settings.getStringOrNull(userIdKey)
        set(value) {
            settings.putString(userIdKey, value)
        }

    override var selectedKeysCount: Int
        get() = settings.getIntOrDefault(selectedKeysCountKey, 0)
        set(value) {
            settings.putInt(selectedKeysCountKey, value)
        }
}