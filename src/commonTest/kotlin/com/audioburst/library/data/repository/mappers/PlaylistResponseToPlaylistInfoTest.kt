package com.audioburst.library.data.repository.mappers

import com.audioburst.library.data.repository.models.ImageResponse
import com.audioburst.library.data.repository.models.PlaylistsResponse
import com.audioburst.library.data.storage.UserStorage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlaylistResponseToPlaylistInfoTest {

    private val userId = "userId"
    private val mapper = PlaylistResponseToPlaylistInfoMapper()
    private val simpleUrl = "https://sapi.audioburst.com/"

    @Test
    fun testMapper() {
        // GIVEN
        val playlistsResponse = playlistsResponseOf(url = simpleUrl)

        // WHEN
        val mapped = mapper.map(playlistsResponse, userId)

        // THEN
        assertEquals(playlistsResponse.playlistId, mapped.id)
        assertEquals(playlistsResponse.playlistName, mapped.name)
        assertEquals(playlistsResponse.description, mapped.description)
        assertEquals(playlistsResponse.image?.thumbnail, mapped.image)
    }

    @Test
    fun testIfUrlContainsUserId() {
        // GIVEN
        val url = "https://sapi.audioburst.com/v2/topstories/category?category=1&appKey=AndroidApp&userId=CrowdTask"
        val playlistsResponse = playlistsResponseOf(url = url)

        // WHEN
        val mapped = mapper.map(playlistsResponse, userId)

        // THEN
        assertTrue(mapped.url.contains("userId=userId"))
    }
}

internal fun playlistsResponseOf(
    playlistId: Int = 0,
    playlistName: String = "",
    description: String = "",
    image: ImageResponse? = null,
    url: String = "",
) : PlaylistsResponse =
    PlaylistsResponse(
        playlistId = playlistId,
        playlistName = playlistName,
        description = description,
        image = image,
        url = url,
    )

internal fun userStorageOf(userId: String? = null): UserStorage =
    object : UserStorage {
        override var userId: String? = userId
    }

internal class InMemoryUserStorage : UserStorage {
    override var userId: String? = null
}
