package com.audioburst.library.utils

internal interface UserStorage {

    var userId: String?

    var deviceId: String?
}

internal class SettingsUserStorage : UserStorage {

    // TODO: Implement it as a values kept in app settings
    override var userId: String? = null

    override var deviceId: String? = null
}
