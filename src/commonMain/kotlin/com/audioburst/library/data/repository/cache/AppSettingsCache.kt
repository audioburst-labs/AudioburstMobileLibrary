package com.audioburst.library.data.repository.cache

import com.audioburst.library.models.AppSettings
import com.audioburst.library.utils.getValue
import com.audioburst.library.utils.nullableAtomic
import com.audioburst.library.utils.setValue

internal class AppSettingsCache: Cache<Unit, AppSettings> {

    private var appSettings by nullableAtomic<AppSettings>()

    override suspend fun get(key: Unit): AppSettings? = appSettings

    override suspend fun set(key: Unit, value: AppSettings) {
        appSettings = value
    }
}