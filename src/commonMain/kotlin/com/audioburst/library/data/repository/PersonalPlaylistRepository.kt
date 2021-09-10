package com.audioburst.library.data.repository

import com.audioburst.library.data.Resource
import com.audioburst.library.data.execute
import com.audioburst.library.data.map
import com.audioburst.library.data.remote.AudioburstV2Api
import com.audioburst.library.data.repository.mappers.PreferenceToUserPreferenceResponseMapper
import com.audioburst.library.data.repository.mappers.TopStoryResponseToPendingPlaylist
import com.audioburst.library.data.repository.mappers.TopStoryResponseToPlaylistResult
import com.audioburst.library.data.repository.mappers.UserPreferenceResponseToPreferenceMapper
import com.audioburst.library.data.repository.models.PostUserPreferenceResponse
import com.audioburst.library.data.repository.models.TopStoryResponse
import com.audioburst.library.data.repository.models.UserPreferenceResponse
import com.audioburst.library.data.result
import com.audioburst.library.models.*
import com.audioburst.library.utils.PlayerSessionIdGetter
import io.ktor.client.*

internal interface PersonalPlaylistRepository {

    suspend fun getUserPreferences(user: User): Resource<UserPreferences>

    suspend fun postUserPreferences(user: User, userPreferences: UserPreferences): Resource<UserPreferences>

    suspend fun getPersonalPlaylistQueryId(user: User): Resource<PlaylistResult>

    suspend fun getPersonalPlaylist(user: User, playerAction: PlayerAction, playerSessionId: PlayerSessionId, playlistQueryId: PlaylistQueryId): Resource<PendingPlaylist>
}

internal class HttpPersonalPlaylistRepository(
    private val httpClient: HttpClient,
    private val audioburstV2Api: AudioburstV2Api,
    private val userPreferenceResponseToPreferenceMapper: UserPreferenceResponseToPreferenceMapper,
    private val preferenceToUserPreferenceResponseMapper: PreferenceToUserPreferenceResponseMapper,
    private val topStoryResponseToPendingPlaylist: TopStoryResponseToPendingPlaylist,
    private val appSettingsRepository: AppSettingsRepository,
    private val topStoryResponseToPlaylistResult: TopStoryResponseToPlaylistResult,
    private val playerSessionIdGetter: PlayerSessionIdGetter,
) : PersonalPlaylistRepository {

    override suspend fun getUserPreferences(user: User): Resource<UserPreferences> =
        httpClient.execute<UserPreferenceResponse>(audioburstV2Api.getUserPreferences(userId = user.userId))
            .supplyIconUrls()

    override suspend fun postUserPreferences(user: User, userPreferences: UserPreferences): Resource<UserPreferences> =
        httpClient.execute<PostUserPreferenceResponse>(
            audioburstV2Api.setUserPreferences(
                userId = user.userId,
                userPreferences = preferenceToUserPreferenceResponseMapper.map(userPreferences)
            )
        ).map(PostUserPreferenceResponse::preferences).supplyIconUrls()

    private suspend fun Resource<UserPreferenceResponse>.supplyIconUrls(): Resource<UserPreferences> =
        map {
            val preferenceImages = appSettingsRepository.getAppSettings().result()?.preferenceImages ?: emptyList()
            userPreferenceResponseToPreferenceMapper.map(it, preferenceImages)
        }

    override suspend fun getPersonalPlaylistQueryId(user: User): Resource<PlaylistResult> =
        httpClient.execute<TopStoryResponse>(audioburstV2Api.getPersonalPlaylistQueryId(userId = user.userId))
            .map {
                topStoryResponseToPlaylistResult.map(
                    from = it,
                    userId = user.userId,
                    playerSessionId = playerSessionIdGetter.get(),
                    playerAction = PlayerAction(
                        type = PlayerAction.Type.Personalized,
                        value = it.queryID.toString(),
                    )
                )
            }

    override suspend fun getPersonalPlaylist(user: User, playerAction: PlayerAction, playerSessionId: PlayerSessionId, playlistQueryId: PlaylistQueryId): Resource<PendingPlaylist> =
        httpClient.execute<TopStoryResponse>(audioburstV2Api.getPersonalPlaylist(playlistQueryId))
            .map {
                topStoryResponseToPendingPlaylist.map(
                    from = it,
                    userId = user.userId,
                    playerAction = playerAction,
                    playerSessionId = playerSessionId,
                )
            }
}
