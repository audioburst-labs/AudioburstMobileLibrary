package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.PlaylistsResponse
import com.audioburst.library.models.PlaylistInfo
import io.ktor.http.*

internal class PlaylistResponseToPlaylistInfoMapper {

    fun map(from: PlaylistsResponse, userId: String): PlaylistInfo =
        PlaylistInfo(
            id = from.playlistId,
            name = from.playlistName,
            description = from.description,
            image = from.image?.thumbnail,
            url = from.url(userId),
        )

    private fun PlaylistsResponse.url(userId: String): String =
        URLBuilder(url).apply {
            parameters.apply {
                remove(USER_ID_QUERY_NAME)
                append(USER_ID_QUERY_NAME, userId)
            }
        }.buildString()

    companion object {
        private const val USER_ID_QUERY_NAME = "userId"
    }
}
