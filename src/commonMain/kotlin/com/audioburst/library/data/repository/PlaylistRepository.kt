package com.audioburst.library.data.repository

import com.audioburst.library.data.Resource
import com.audioburst.library.data.execute
import com.audioburst.library.data.map
import com.audioburst.library.data.remote.AudioburstV2Api
import com.audioburst.library.data.remote.Endpoint
import com.audioburst.library.data.remote.buildUrl
import com.audioburst.library.data.repository.mappers.TopStoryResponseToPlaylist
import com.audioburst.library.data.repository.mappers.TopStoryResponseToPlaylistResult
import com.audioburst.library.data.repository.models.TopStoryResponse
import com.audioburst.library.models.*
import com.audioburst.library.utils.LibraryConfiguration
import com.audioburst.library.utils.PlayerSessionIdGetter
import io.ktor.client.*

internal interface PlaylistRepository {

    suspend fun channel(playlistRequest: PlaylistRequest.Channel, userId: String): Resource<Playlist>

    suspend fun userGenerated(playlistRequest: PlaylistRequest.UserGenerated, userId: String): Resource<Playlist>

    suspend fun source(playlistRequest: PlaylistRequest.Source, userId: String): Resource<Playlist>

    suspend fun account(playlistRequest: PlaylistRequest.Account, userId: String): Resource<Playlist>

    suspend fun getPlaylist(userId: String, playlistInfo: PlaylistInfo): Resource<Playlist>

    suspend fun search(userId: String, byteArray: ByteArray): Resource<PlaylistResult>

    suspend fun search(userId: String, query: String): Resource<PlaylistResult>

    fun url(userId: String, playerAction: PlayerAction): Url?
}

internal class HttpPlaylistRepository(
    private val httpClient: HttpClient,
    private val audioburstV2Api: AudioburstV2Api,
    private val libraryConfiguration: LibraryConfiguration,
    private val topStoryResponseToPlaylist: TopStoryResponseToPlaylist,
    private val playerSessionIdGetter: PlayerSessionIdGetter,
    private val topStoryResponseToPlaylistResult: TopStoryResponseToPlaylistResult,
) : PlaylistRepository {

    private suspend fun getPlaylist(
        endpoint: Endpoint,
        userId: String,
        playlistRequest: PlaylistRequest,
    ): Resource<Playlist> =
        httpClient.execute<TopStoryResponse>(endpoint = endpoint).map { topStoryResponse ->
            topStoryResponseToPlaylist.map(
                from = topStoryResponse,
                userId = userId,
                playlistId = playlistRequest.playerAction.value,
                playlistName = playlistRequest.playlistName,
                playerAction = playlistRequest.playerAction,
            )
        }

    override suspend fun channel(playlistRequest: PlaylistRequest.Channel, userId: String): Resource<Playlist> =
        getPlaylist(
            endpoint = audioburstV2Api.getChannelPlaylist(
                userId = userId,
                device = DEVICE,
                id = playlistRequest.id,
            ),
            userId = userId,
            playlistRequest = playlistRequest,
        )

    override suspend fun userGenerated(playlistRequest: PlaylistRequest.UserGenerated, userId: String): Resource<Playlist> =
        getPlaylist(
            endpoint = audioburstV2Api.getUserGeneratedPlaylist(
                userId = userId,
                device = DEVICE,
                id = playlistRequest.id,
            ),
            userId = userId,
            playlistRequest = playlistRequest,
        )

    override suspend fun source(playlistRequest: PlaylistRequest.Source, userId: String): Resource<Playlist> =
        getPlaylist(
            endpoint = audioburstV2Api.getSourcePlaylist(
                userId = userId,
                device = DEVICE,
                id = playlistRequest.id,
            ),
            userId = userId,
            playlistRequest = playlistRequest,
        )

    override suspend fun account(playlistRequest: PlaylistRequest.Account, userId: String): Resource<Playlist> =
        getPlaylist(
            endpoint = audioburstV2Api.getAccountPlaylist(
                userId = userId,
                device = DEVICE,
                id = playlistRequest.id,
            ),
            userId = userId,
            playlistRequest = playlistRequest,
        )

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

    override suspend fun search(userId: String, byteArray: ByteArray): Resource<PlaylistResult> =
        httpClient.execute<TopStoryResponse>(
            audioburstV2Api.getPlaylist(
                byteArray = byteArray,
                userId = userId,
                libraryKey = libraryConfiguration.libraryKey,
            )
        ).map { topStoryResponse ->
            topStoryResponseToPlaylistResult.map(
                from = topStoryResponse,
                userId = userId,
                playerAction = PlayerAction(
                    type = PlayerAction.Type.Voice,
                    value = topStoryResponse.actualQuery ?: "",
                ),
                playerSessionId = playerSessionIdGetter.get(),
            )
        }

    override suspend fun search(userId: String, query: String): Resource<PlaylistResult> =
        httpClient.execute<TopStoryResponse>(
            audioburstV2Api.search(
                query = query,
                userId = userId,
            )
        ).map { topStoryResponse ->
            topStoryResponseToPlaylistResult.map(
                from = topStoryResponse,
                userId = userId,
                playerAction = PlayerAction(
                    type = PlayerAction.Type.Search,
                    value = query,
                ),
                playerSessionId = playerSessionIdGetter.get(),
            )
        }

    override fun url(userId: String, playerAction: PlayerAction): Url? =
        when (playerAction.type) {
            PlayerAction.Type.Channel -> playerAction.value.toIntOrNull()?.let { id ->
                audioburstV2Api.getChannelPlaylist(
                    id = id,
                    userId = userId,
                    device = DEVICE,
                )
            }
            PlayerAction.Type.UserGenerated -> audioburstV2Api.getUserGeneratedPlaylist(
                userId = userId,
                device = DEVICE,
                id = playerAction.value,
            )
            PlayerAction.Type.Account -> audioburstV2Api.getSourcePlaylist(
                userId = userId,
                device = DEVICE,
                id = playerAction.value,
            )
            PlayerAction.Type.Source -> audioburstV2Api.getAccountPlaylist(
                userId = userId,
                device = DEVICE,
                id = playerAction.value,
            )
            PlayerAction.Type.Personalized, PlayerAction.Type.Voice, PlayerAction.Type.Search -> null
        }?.buildUrl()

    companion object {
        private const val DEVICE = "mobile"
    }
}