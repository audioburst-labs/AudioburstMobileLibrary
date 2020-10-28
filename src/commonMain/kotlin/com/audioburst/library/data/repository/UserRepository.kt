package com.audioburst.library.data.repository

import com.audioburst.library.data.Resource
import com.audioburst.library.data.execute
import com.audioburst.library.data.map
import com.audioburst.library.data.onData
import com.audioburst.library.data.remote.AbAiRouterApi
import com.audioburst.library.data.remote.AudioburstApi
import com.audioburst.library.data.remote.AudioburstV2Api
import com.audioburst.library.data.repository.mappers.*
import com.audioburst.library.data.repository.models.PlaylistsResponse
import com.audioburst.library.data.repository.models.PromoteResponse
import com.audioburst.library.data.repository.models.RegisterResponse
import com.audioburst.library.data.repository.models.TopStoryResponse
import com.audioburst.library.data.storage.PlaylistStorage
import com.audioburst.library.models.*
import com.audioburst.library.utils.LibraryConfiguration
import io.ktor.client.*

internal interface UserRepository {
    suspend fun registerUser(userId: String): Resource<User>

    suspend fun getPlaylists(userId: String): Resource<List<PlaylistInfo>>

    suspend fun getPlaylist(userId: String, playlistInfo: PlaylistInfo): Resource<Playlist>

    suspend fun postEvent(playerEvent: PlayerEvent, name: String): Resource<Unit>

    suspend fun postEvent(advertisementEvent: AdvertisementEvent): Resource<Unit>

    suspend fun postReportingData(reportingData: ReportingData): Resource<Unit>

    suspend fun postBurstPlayback(playlistId: Long, burstId: String, userId: String): Resource<Unit>

    suspend fun getAdData(adUrl: Url): Resource<Advertisement>
}

internal class HttpUserRepository(
    private val httpClient: HttpClient,
    private val audioburstV2Api: AudioburstV2Api,
    private val audioburstApi: AudioburstApi,
    private val abAiRouterApi: AbAiRouterApi,
    private val libraryConfiguration: LibraryConfiguration,
    private val registerResponseToUserMapper: RegisterResponseToUserMapper,
    private val playlistResponseToPlaylistInfoMapper: PlaylistResponseToPlaylistInfoMapper,
    private val topStoryResponseToPlaylist: TopStoryResponseToPlaylist,
    private val promoteResponseToAdvertisementMapper: PromoteResponseToAdvertisementMapper,
    private val advertisementEventToAdvertisementEventRequestMapper: AdvertisementEventToAdvertisementEventRequestMapper,
    private val playerEventToEventRequestMapper: PlayerEventToEventRequestMapper,
    private val playlistStorage: PlaylistStorage,
) : UserRepository {

    override suspend fun registerUser(userId: String): Resource<User> =
        httpClient.execute<RegisterResponse>(audioburstV2Api.registerUser(userId)).map(registerResponseToUserMapper::map)

    override suspend fun getPlaylists(userId: String): Resource<List<PlaylistInfo>> =
        httpClient.execute<List<PlaylistsResponse>>(audioburstV2Api.getAllPlaylists()).map { playlistResponses ->
            playlistResponses.map { playlistResponseToPlaylistInfoMapper.map(it, userId) }
        }

    override suspend fun getPlaylist(userId: String, playlistInfo: PlaylistInfo): Resource<Playlist> =
        httpClient.execute<TopStoryResponse>(Url(playlistInfo.url))
            .map { topStoryResponseToPlaylist.map(it, userId, playlistInfo) }
            .onData(playlistStorage::setPlaylist)

    override suspend fun postEvent(playerEvent: PlayerEvent, name: String): Resource<Unit> =
        httpClient.execute(
            endpoint = abAiRouterApi.postEvent(
                eventRequest = playerEventToEventRequestMapper.map(playerEvent),
                name = name
            )
        )

    override suspend fun postEvent(advertisementEvent: AdvertisementEvent): Resource<Unit> =
        httpClient.execute(
            endpoint = abAiRouterApi.postEvent(
                advertisementEventRequest = advertisementEventToAdvertisementEventRequestMapper.map(advertisementEvent)
            )
        )

    override suspend fun postReportingData(reportingData: ReportingData): Resource<Unit> =
        httpClient.execute(Url(reportingData.url))

    override suspend fun postBurstPlayback(playlistId: Long, burstId: String, userId: String): Resource<Unit> =
        httpClient.execute(
            audioburstApi.getBurstPlay(
                userId = userId,
                burstId = burstId,
                libraryKey = libraryConfiguration.libraryKey,
                playlistId = playlistId,
                downloadType = LOG_ONLY_DOWNLOAD_TYPE,
                subscriptionKey = libraryConfiguration.subscriptionKey,
            )
        )

    override suspend fun getAdData(adUrl: Url): Resource<Advertisement> =
        httpClient.execute<PromoteResponse>(adUrl)
            .map(promoteResponseToAdvertisementMapper::map)
            .onData {
                playlistStorage.setAdvertisement(
                    url = adUrl,
                    advertisement = it
                )
        }

    companion object {
        private const val LOG_ONLY_DOWNLOAD_TYPE = 2
    }
}
