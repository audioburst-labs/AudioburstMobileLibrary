package com.audioburst.library.data.repository

import com.audioburst.library.data.Resource
import com.audioburst.library.data.httpClientOf
import com.audioburst.library.data.remote.AudioburstV2Api
import com.audioburst.library.data.repository.mappers.*
import com.audioburst.library.data.repository.models.AsyncQueryIdResponse
import com.audioburst.library.data.repository.models.PostUserPreferenceResponse
import com.audioburst.library.data.repository.models.UserPreferenceResponse
import com.audioburst.library.interactors.playlistStorageOf
import com.audioburst.library.interactors.resourceErrorOf
import com.audioburst.library.interactors.userOf
import com.audioburst.library.models.AppSettings
import com.audioburst.library.models.PendingPlaylist
import com.audioburst.library.models.PersonalPlaylistQueryId
import com.audioburst.library.models.ShareTexts
import com.audioburst.library.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test

class HttpPersonalPlaylistRepositoryTest {

    private inline fun <reified T> repository(
        httpClientReturns: Resource<T>,
        appSettingsRepositoryReturns: Resource<AppSettings> = resourceErrorOf(),
        audioburstV2Api: AudioburstV2Api = audioburstV2ApiOf(),
        userPreferenceResponseToPreferenceMapper: UserPreferenceResponseToPreferenceMapper = UserPreferenceResponseToPreferenceMapper(),
        preferenceToUserPreferenceResponseMapper: PreferenceToUserPreferenceResponseMapper = PreferenceToUserPreferenceResponseMapper(),
        topStoryResponseToPendingPlaylist: TopStoryResponseToPendingPlaylist = topStoryResponseToPendingPlaylistOf(),
    ): HttpPersonalPlaylistRepository =
        HttpPersonalPlaylistRepository(
            httpClient = httpClientOf(httpClientReturns),
            audioburstV2Api = audioburstV2Api,
            userPreferenceResponseToPreferenceMapper = userPreferenceResponseToPreferenceMapper,
            preferenceToUserPreferenceResponseMapper = preferenceToUserPreferenceResponseMapper,
            topStoryResponseToPendingPlaylist = topStoryResponseToPendingPlaylist,
            appSettingsRepository = appSettingsRepositoryOf(appSettingsRepositoryReturns),
            playlistStorage = playlistStorageOf(),
        )

    @Test
    fun testIfGetPersonalPlaylistQueryIdReturnsErrorWhenHttpClientReturnsError() = runTest {
        // GIVEN
        val response = resourceErrorOf()

        // WHEN
        val resource = repository<PersonalPlaylistQueryId>(
            httpClientReturns = response
        ).getPersonalPlaylistQueryId(userOf())

        // THEN
        require(resource is Resource.Error)
    }

    @Test
    fun testIfGetPersonalPlaylistQueryIdReturnsDataWhenHttpClientReturnsData() = runTest {
        // GIVEN
        val response = Resource.Data(AsyncQueryIdResponse(0))

        // WHEN
        val resource = repository(
            httpClientReturns = response
        ).getPersonalPlaylistQueryId(userOf())

        // THEN
        require(resource is Resource.Data)
    }

    @Test
    fun testIfGetPersonalPlaylistReturnsErrorWhenHttpClientReturnsError() = runTest {
        // GIVEN
        val response = resourceErrorOf()

        // WHEN
        val resource = repository<PendingPlaylist>(
            httpClientReturns = response
        ).getPersonalPlaylist(userOf(), personalPlaylistQueryId = PersonalPlaylistQueryId(0))

        // THEN
        require(resource is Resource.Error)
    }

    @Test
    fun testIfGetPersonalPlaylistReturnsDataWhenHttpClientReturnsData() = runTest {
        // GIVEN
        val response = Resource.Data(topStoryResponseOf())

        // WHEN
        val resource = repository(
            httpClientReturns = response
        ).getPersonalPlaylist(userOf(), personalPlaylistQueryId = PersonalPlaylistQueryId(0))

        // THEN
        require(resource is Resource.Data)
    }

    @Test
    fun testIfGetUserPreferencesErrorWhenHttpClientReturnsError() = runTest {
        // GIVEN
        val response = resourceErrorOf()

        // WHEN
        val resource = repository<UserPreferenceResponse>(
            httpClientReturns = response
        ).getUserPreferences(userOf())

        // THEN
        require(resource is Resource.Error)
    }

    @Test
    fun testIfGetUserPreferencesReturnsDataWhenHttpClientReturnsData() = runTest {
        // GIVEN
        val response = Resource.Data(userPreferenceResponseOf())

        // WHEN
        val resource = repository(
            httpClientReturns = response
        ).getUserPreferences(userOf())

        // THEN
        require(resource is Resource.Data)
    }

    @Test
    fun testIfGetUserPreferencesReturnsDataWhenHttpClientReturnsDataAndEvenIfAppSettingsRepositoryReturnsError() = runTest {
        // GIVEN
        val appSettingsRepositoryReturns = resourceErrorOf()
        val response = Resource.Data(userPreferenceResponseOf())

        // WHEN
        val resource = repository(
            httpClientReturns = response,
            appSettingsRepositoryReturns = appSettingsRepositoryReturns,
        ).getUserPreferences(userOf())

        // THEN
        require(resource is Resource.Data)
    }

    @Test
    fun testIfPostUserPreferencesErrorWhenHttpClientReturnsError() = runTest {
        // GIVEN
        val response = resourceErrorOf()

        // WHEN
        val resource = repository<PostUserPreferenceResponse>(
            httpClientReturns = response
        ).postUserPreferences(userOf(), userPreferenceOf())

        // THEN
        require(resource is Resource.Error)
    }

    @Test
    fun testIfPostUserPreferencesReturnsDataWhenHttpClientReturnsData() = runTest {
        // GIVEN
        val response = Resource.Data(PostUserPreferenceResponse(success = true, userPreferenceResponseOf()))

        // WHEN
        val resource = repository(
            httpClientReturns = response
        ).postUserPreferences(userOf(), userPreferenceOf())

        // THEN
        require(resource is Resource.Data)
    }

    @Test
    fun testIfPostUserPreferencesReturnsDataWhenHttpClientReturnsDataAndEvenIfAppSettingsRepositoryReturnsError() = runTest {
        // GIVEN
        val appSettingsRepositoryReturns = resourceErrorOf()
        val response = Resource.Data(PostUserPreferenceResponse(success = true, userPreferenceResponseOf()))

        // WHEN
        val resource = repository(
            httpClientReturns = response,
            appSettingsRepositoryReturns = appSettingsRepositoryReturns,
        ).postUserPreferences(userOf(), userPreferenceOf())

        // THEN
        require(resource is Resource.Data)
    }
}

internal fun appSettingsRepositoryOf(
    resource: Resource<AppSettings> = Resource.Data(appSettingsOf()),
    shareTexts: ShareTexts = shareTextsOf()
): AppSettingsRepository =
    object : AppSettingsRepository {
        override suspend fun getAppSettings(): Resource<AppSettings> = resource
        override suspend fun getShareTexts(): ShareTexts = shareTexts
    }

internal fun shareTextsOf(
    burst: String = "",
    playlist: String = "",
): ShareTexts = ShareTexts(
    burst = burst,
    playlist = playlist,
)

internal fun audioburstV2ApiOf(json: Json = Json {  }): AudioburstV2Api = AudioburstV2Api(json)