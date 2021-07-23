package com.audioburst.library.data.repository

import com.audioburst.library.data.Resource
import com.audioburst.library.data.httpClientOf
import com.audioburst.library.data.remote.AudioburstStorageApi
import com.audioburst.library.data.repository.cache.Cache
import com.audioburst.library.data.repository.mappers.AppSettingsResponseToAppSettingsMapper
import com.audioburst.library.data.repository.mappers.appSettingsResponseOf
import com.audioburst.library.data.repository.models.AppSettingsResponse
import com.audioburst.library.interactors.resourceErrorOf
import com.audioburst.library.models.AppSettings
import com.audioburst.library.models.PreferenceImage
import com.audioburst.library.models.ShareTexts
import com.audioburst.library.models.Url
import com.audioburst.library.runTest
import io.ktor.client.*
import kotlin.test.Test

class CachedAppSettingsRepositoryTest {

    private fun repository(
        httpClient: HttpClient,
        audioburstStorageApi: AudioburstStorageApi = AudioburstStorageApi(),
        appSettingsCache: Cache<Unit, AppSettings> = cacheOf(null),
        appSettingsResponseToAppSettingsMapper: AppSettingsResponseToAppSettingsMapper = AppSettingsResponseToAppSettingsMapper(),
    ): CachedAppSettingsRepository =
        CachedAppSettingsRepository(
            httpClient = httpClient,
            audioburstStorageApi = audioburstStorageApi,
            appSettingsCache = appSettingsCache,
            appSettingsResponseToAppSettingsMapper = appSettingsResponseToAppSettingsMapper,
        )

    private fun repository(
        httpResponse: Resource<AppSettingsResponse>,
        appSettingsCache: AppSettings? = null,
    ): CachedAppSettingsRepository =
        repository(
            httpClient = httpClientOf(httpResponse),
            appSettingsCache = cacheOf(appSettingsCache)
        )

    @Test
    fun testIfRepositoryReturnsErrorWhenHttpClientReturnsErrorAndThereIsNoCachedValue() = runTest {
        // GIVEN
        val response = resourceErrorOf()

        // WHEN
        val resource = repository(
            httpResponse = response,
            appSettingsCache = null,
        ).getAppSettings()

        // THEN
        require(resource is Resource.Error)
    }

    @Test
    fun testIfRepositoryReturnsDataWhenHttpClientReturnsDataAndThereIsNoCachedValue() = runTest {
        // GIVEN
        val appSettingsResponse = appSettingsResponseOf()
        val response = Resource.Data(appSettingsResponse)

        // WHEN
        val resource = repository(
            httpResponse = response,
            appSettingsCache = null,
        ).getAppSettings()

        // THEN
        require(resource is Resource.Data)
    }

    @Test
    fun testIfRepositoryReturnsDataFromCache() = runTest {
        // GIVEN
        val appSettingsCache = appSettingsOf()
        val response = resourceErrorOf()

        // WHEN
        val resource = repository(
            httpResponse = response,
            appSettingsCache = appSettingsCache,
        ).getAppSettings()

        // THEN
        require(resource is Resource.Data)
    }
}

internal fun <KEY, VALUE> cacheOf(value: VALUE?): Cache<KEY, VALUE> =
    object : Cache<KEY, VALUE> {
        override suspend fun get(key: KEY): VALUE? = value
        override suspend fun set(key: KEY, value: VALUE) = Unit
    }

internal fun appSettingsOf(
    preferences: List<PreferenceImage> = emptyList(),
    shareTexts: ShareTexts = shareTextsOf(),
): AppSettings = AppSettings(
    preferenceImages = preferences,
    shareTexts = shareTexts,
)

internal fun preferenceImageOf(
    name: String = "",
    imageUrl: String = "",
): PreferenceImage =
    PreferenceImage(
        name = name,
        imageUrl = Url(imageUrl),
    )