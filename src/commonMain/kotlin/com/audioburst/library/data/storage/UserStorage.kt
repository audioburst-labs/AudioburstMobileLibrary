package com.audioburst.library.data.storage

import com.audioburst.library.utils.Settings

internal interface UserStorage {

    var userId: String?
}

internal class SettingsUserStorage(
    settingsName: String,
    private val settings: Settings
) : UserStorage {

    private val userIdKey = "$settingsName.userId"

    override var userId: String?
        get() = settings.getStringOrNull(userIdKey)
        set(value) {
            settings.putString(userIdKey, value)
        }
}