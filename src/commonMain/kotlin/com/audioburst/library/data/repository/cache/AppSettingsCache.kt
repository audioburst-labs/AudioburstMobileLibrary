package com.audioburst.library.data.repository.cache

import co.touchlab.stately.concurrency.AtomicReference
import com.audioburst.library.models.AppSettings

internal class AppSettingsCache: Cache<Unit, AppSettings> {

    private val appSettings: AtomicReference<AppSettings?> = AtomicReference(null)

    override suspend fun get(key: Unit): AppSettings? = appSettings.get()

    override suspend fun set(key: Unit, value: AppSettings) {
        appSettings.set(value)
    }
}