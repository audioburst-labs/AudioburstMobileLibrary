package com.audioburst.library.data.repository

import com.audioburst.library.data.Resource
import com.audioburst.library.data.execute
import com.audioburst.library.data.map
import com.audioburst.library.data.remote.AbAiRouterApi
import com.audioburst.library.data.remote.AudioburstApi
import com.audioburst.library.data.remote.AudioburstV2Api
import com.audioburst.library.data.repository.mappers.*
import com.audioburst.library.data.repository.models.AdvertisementResponse
import com.audioburst.library.data.repository.models.PlaylistsResponse
import com.audioburst.library.data.repository.models.TopStoryResponse
import com.audioburst.library.data.repository.models.UserResponse
import com.audioburst.library.models.*
import com.audioburst.library.utils.LibraryConfiguration
import io.ktor.client.*

internal interface UserRepository {
    suspend fun registerUser(userId: String): Resource<User>

    suspend fun verifyUserId(userId: String): Resource<User>

    suspend fun getPlaylists(userId: String): Resource<List<PlaylistInfo>>

    suspend fun getPlaylist(userId: String, playlistInfo: PlaylistInfo): Resource<Playlist>

    suspend fun getPlaylist(userId: String, byteArray: ByteArray): Resource<Playlist>

    suspend fun search(userId: String, query: String): Resource<Playlist>

    suspend fun postEvent(playerEvent: PlayerEvent, name: String): Resource<Unit>

    suspend fun postReportingData(reportingData: ReportingData): Resource<Unit>

    suspend fun postBurstPlayback(playlistId: Long, burstId: String, userId: String): Resource<Unit>

    suspend fun getPromoteData(adUrl: Url): Resource<PromoteData>
}

internal class HttpUserRepository(
    private val httpClient: HttpClient,
    private val audioburstV2Api: AudioburstV2Api,
    private val audioburstApi: AudioburstApi,
    private val abAiRouterApi: AbAiRouterApi,
    private val libraryConfiguration: LibraryConfiguration,
    private val userResponseToUserMapper: UserResponseToUserMapper,
    private val playlistResponseToPlaylistInfoMapper: PlaylistResponseToPlaylistInfoMapper,
    private val topStoryResponseToPlaylist: TopStoryResponseToPlaylist,
    private val advertisementResponseToAdvertisementMapper: AdvertisementResponseToPromoteDataMapper,
    private val playerEventToEventRequestMapper: PlayerEventToEventRequestMapper,
) : UserRepository {

    override suspend fun registerUser(userId: String): Resource<User> =
        httpClient.execute<UserResponse>(audioburstV2Api.registerUser(userId)).map(userResponseToUserMapper::map)

    override suspend fun verifyUserId(userId: String): Resource<User> =
        httpClient.execute<UserResponse>(audioburstV2Api.verify(userId)).map(userResponseToUserMapper::map)

    override suspend fun getPlaylists(userId: String): Resource<List<PlaylistInfo>> =
        httpClient.execute<List<PlaylistsResponse>>(audioburstV2Api.getAllPlaylists()).map { playlistResponses ->
            playlistResponses.map { playlistResponseToPlaylistInfoMapper.map(it, userId) }
        }

    override suspend fun getPlaylist(userId: String, playlistInfo: PlaylistInfo): Resource<Playlist> =
        httpClient.execute<TopStoryResponse>(Url(playlistInfo.url))
            .map { topStoryResponse ->
                topStoryResponseToPlaylist.map(
                    from = topStoryResponse,
                    userId = userId,
                    playlistId = playlistInfo.id.toString(),
                    playlistName = playlistInfo.name,
                    playerAction = PlayerAction(
                        type = PlayerAction.Type.Channel,
                        value = playlistInfo.id.toString(),
                    )
                )
            }

    override suspend fun getPlaylist(userId: String, byteArray: ByteArray): Resource<Playlist> =
        httpClient.execute<TopStoryResponse>(
            audioburstV2Api.getPlaylist(
                byteArray = byteArray,
                userId = userId,
                libraryKey = libraryConfiguration.libraryKey,
            )
        ).map { topStoryResponse ->
            topStoryResponseToPlaylist.map(
                from = topStoryResponse,
                userId = userId,
                playlistId = "",
                playlistName = "",
                playerAction = PlayerAction(
                    type = PlayerAction.Type.Voice,
                    value = topStoryResponse.actualQuery ?: "",
                )
            )
        }

    override suspend fun search(userId: String, query: String): Resource<Playlist> =
        httpClient.execute<TopStoryResponse>(
            audioburstV2Api.search(
                query = query,
                userId = userId,
            )
        ).map { topStoryResponse ->
            topStoryResponseToPlaylist.map(
                from = topStoryResponse,
                userId = userId,
                playlistId = "",
                playlistName = topStoryResponse.actualQuery ?: query,
                playerAction = PlayerAction(
                    type = PlayerAction.Type.Search,
                    value = query,
                )
            )
        }

    override suspend fun postEvent(playerEvent: PlayerEvent, name: String): Resource<Unit> =
        httpClient.execute(
            endpoint = abAiRouterApi.postEvent(
                eventRequest = playerEventToEventRequestMapper.map(playerEvent),
                name = name
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

    override suspend fun getPromoteData(adUrl: Url): Resource<PromoteData> =
        httpClient.execute<AdvertisementResponse>(adUrl)
            .map(advertisementResponseToAdvertisementMapper::map)

    companion object {
        private const val LOG_ONLY_DOWNLOAD_TYPE = 2
    }
}
