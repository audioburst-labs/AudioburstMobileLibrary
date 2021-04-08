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
        assertEquals(playlistsResponse.section, mapped.section)
        assertEquals(playlistsResponse.playlistId, mapped.id)
        assertEquals(playlistsResponse.playlistName, mapped.name)
        assertEquals(playlistsResponse.description, mapped.description)
        assertEquals(playlistsResponse.image.thumbnail, mapped.image)
        assertEquals(playlistsResponse.image.square, mapped.squareImage)
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
    section: String = "",
    playlistId: Int = 0,
    playlistName: String = "",
    description: String = "",
    image: ImageResponse = imageResponseOf(),
    url: String = "",
) : PlaylistsResponse =
    PlaylistsResponse(
        section = section,
        playlistId = playlistId,
        playlistName = playlistName,
        description = description,
        image = image,
        url = url,
    )

internal fun imageResponseOf(
    url: String = "",
    thumbnail: String = "",
    svg: String = "",
    square: String = "",
): ImageResponse =
    ImageResponse(
        url = url,
        thumbnail = thumbnail,
        svg = svg,
        square = square,
    )

internal fun userStorageOf(userId: String? = null, selectedKeysCount: Int = 0, filterListenedBursts: Boolean = false): UserStorage =
    object : UserStorage {
        override var userId: String? = userId
        override var selectedKeysCount: Int = selectedKeysCount
        override var filterListenedBursts: Boolean = filterListenedBursts
    }

internal class InMemoryUserStorage : UserStorage {
    override var userId: String? = null
    override var selectedKeysCount: Int = 0
    override var filterListenedBursts: Boolean = false

    fun clear() {
        userId = null
        selectedKeysCount = 0
        filterListenedBursts = false
    }
}
