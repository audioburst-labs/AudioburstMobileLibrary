package com.audioburst.library.data.repository

import com.audioburst.library.data.*
import com.audioburst.library.data.remote.AudioburstStorageApi
import com.audioburst.library.data.repository.cache.Cache
import com.audioburst.library.data.repository.mappers.AppSettingsResponseToAppSettingsMapper
import com.audioburst.library.data.repository.models.AppSettingsResponse
import com.audioburst.library.models.AppSettings
import com.audioburst.library.models.ShareTexts
import com.audioburst.library.utils.Strings
import io.ktor.client.*

internal interface AppSettingsRepository {

    suspend fun getAppSettings(): Resource<AppSettings>

    suspend fun getShareTexts(): ShareTexts
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

    override suspend fun getShareTexts(): ShareTexts =
        getAppSettings()
            .map { it.shareTexts }
            .onErrorReturns {
                ShareTexts(
                    burst = DEFAULT_SHARE_BURST_TEXT,
                    playlist = DEFAULT_SHARE_PLAYLIST_TEXT,
                )
            }.result

    private suspend fun requestAppSettings(): Resource<AppSettings> =
        httpClient.execute<AppSettingsResponse>(audioburstStorageApi.getMobileSettings())
            .map(appSettingsResponseToAppSettingsMapper::map)

    companion object {
        private const val DEFAULT_SHARE_BURST_TEXT = Strings.DEFAULT_SHARE_BURST_TEXT
        private const val DEFAULT_SHARE_PLAYLIST_TEXT = Strings.DEFAULT_SHARE_PLAYLIST_TEXT
    }
}