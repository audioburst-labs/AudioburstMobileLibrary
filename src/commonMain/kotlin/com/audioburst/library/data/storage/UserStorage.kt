package com.audioburst.library.data.storage

import com.audioburst.library.utils.Settings

internal interface UserStorage {

    var userId: String?

    var selectedKeysCount: Int

    var filterListenedBursts: Boolean
}

internal class SettingsUserStorage(
    settingsName: String,
    private val settings: Settings
) : UserStorage {

    private val userIdKey = "$settingsName.userId"
    private val selectedKeysCountKey = "$settingsName.selectedKeysCount"
    private val filterListenedBurstsKey = "$settingsName.filterListenedBursts"

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

    override var filterListenedBursts: Boolean
        get() = settings.getBooleanOrDefault(filterListenedBurstsKey, true)
        set(value) {
            settings.putBoolean(filterListenedBurstsKey, value)
        }
}