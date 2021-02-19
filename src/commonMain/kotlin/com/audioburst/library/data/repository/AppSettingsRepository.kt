package com.audioburst.library.data.repository

import com.audioburst.library.data.Resource
import com.audioburst.library.data.execute
import com.audioburst.library.data.map
import com.audioburst.library.data.onData
import com.audioburst.library.data.remote.AudioburstStorageApi
import com.audioburst.library.data.repository.cache.Cache
import com.audioburst.library.data.repository.mappers.AppSettingsResponseToAppSettingsMapper
import com.audioburst.library.data.repository.models.AppSettingsResponse
import com.audioburst.library.models.AppSettings
import io.ktor.client.*

internal interface AppSettingsRepository {

    suspend fun getAppSettings(): Resource<AppSettings>
}

internal class CachedAppSettingsRepository(
    private val httpClient: HttpClient,
    private val audioburstStorageApi: AudioburstStorageApi,
    private val appSettingsCache: Cache<Unit, AppSettings>,
    private val appSettingsResponseToAppSettingsMapper: AppSettingsResponseToAppSettingsMapper,
) : AppSettingsRepository {

    override suspend fun getAppSettings(): Resource<AppSettings> =
        appSettingsCache.get(Unit)?.let { Resource.Data(it) } ?: requestAppSettings().onData {
            appSettingsCache.set(Unit, it)
        }

    private suspend fun requestAppSettings(): Resource<AppSettings> =
        httpClient.execute<AppSettingsResponse>(audioburstStorageApi.getMobileSettings())
            .map(appSettingsResponseToAppSettingsMapper::map)
}