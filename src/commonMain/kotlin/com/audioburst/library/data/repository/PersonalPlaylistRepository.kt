package com.audioburst.library.data.repository

import com.audioburst.library.data.Resource
import com.audioburst.library.data.execute
import com.audioburst.library.data.map
import com.audioburst.library.data.remote.AudioburstV2Api
import com.audioburst.library.data.repository.mappers.PreferenceToUserPreferenceResponseMapper
import com.audioburst.library.data.repository.mappers.TopStoryResponseToPendingPlaylist
import com.audioburst.library.data.repository.mappers.UserPreferenceResponseToPreferenceMapper
import com.audioburst.library.data.repository.models.AsyncQueryIdResponse
import com.audioburst.library.data.repository.models.PostUserPreferenceResponse
import com.audioburst.library.data.repository.models.TopStoryResponse
import com.audioburst.library.data.repository.models.UserPreferenceResponse
import com.audioburst.library.models.PendingPlaylist
import com.audioburst.library.models.PersonalPlaylistQueryId
import com.audioburst.library.models.User
import com.audioburst.library.models.UserPreferences
import io.ktor.client.*

internal interface PersonalPlaylistRepository {

    suspend fun getUserPreferences(user: User): Resource<UserPreferences>

    suspend fun postUserPreferences(user: User, userPreferences: UserPreferences): Resource<UserPreferences>

    suspend fun getPersonalPlaylistQueryId(user: User): Resource<PersonalPlaylistQueryId>

    suspend fun getPersonalPlaylist(user: User, personalPlaylistQueryId: PersonalPlaylistQueryId): Resource<PendingPlaylist>
}

internal class HttpPersonalPlaylistRepository(
    private val httpClient: HttpClient,
    private val audioburstV2Api: AudioburstV2Api,
    private val userPreferenceResponseToPreferenceMapper: UserPreferenceResponseToPreferenceMapper,
    private val preferenceToUserPreferenceResponseMapper: PreferenceToUserPreferenceResponseMapper,
    private val topStoryResponseToPendingPlaylist: TopStoryResponseToPendingPlaylist,
) : PersonalPlaylistRepository {

    override suspend fun getUserPreferences(user: User): Resource<UserPreferences> =
        httpClient.execute<UserPreferenceResponse>(audioburstV2Api.getUserPreferences(userId = user.userId))
            .map(userPreferenceResponseToPreferenceMapper::map)

    override suspend fun postUserPreferences(user: User, userPreferences: UserPreferences): Resource<UserPreferences> =
        httpClient.execute<PostUserPreferenceResponse>(
            audioburstV2Api.setUserPreferences(
                userId = user.userId,
                userPreferences = preferenceToUserPreferenceResponseMapper.map(userPreferences)
            )
        ).map { userPreferenceResponseToPreferenceMapper.map(it.preferences) }

    override suspend fun getPersonalPlaylistQueryId(user: User): Resource<PersonalPlaylistQueryId> =
        httpClient.execute<AsyncQueryIdResponse>(audioburstV2Api.getPersonalPlaylistQueryId(userId = user.userId))
            .map { PersonalPlaylistQueryId(it.queryId) }

    override suspend fun getPersonalPlaylist(user: User, personalPlaylistQueryId: PersonalPlaylistQueryId): Resource<PendingPlaylist> =
        httpClient.execute<TopStoryResponse>(audioburstV2Api.getPersonalPlaylist(personalPlaylistQueryId))
            .map {
                topStoryResponseToPendingPlaylist.map(
                    from = it,
                    userId = user.userId
                )
            }
}
