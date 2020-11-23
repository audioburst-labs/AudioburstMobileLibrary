package com.audioburst.library.data.repository.mappers

import com.audioburst.library.models.PlayerSessionId
import com.audioburst.library.utils.PlayerSessionIdGetter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TopStoryResponseToPlaylistTest {

    private val playerSessionId: String = ""
    private val userId = ""

    private val mapper = TopStoryResponseToPlaylist(
        burstResponseToBurstMapper = BurstResponseToBurstMapper(
            libraryConfiguration = libraryConfigurationOf(),
            sourceResponseToBurstSourceMapper = SourceResponseToBurstSourceMapper(),
        ),
        playerSessionIdGetter = playerSessionIdGetterOf(playerSessionId = playerSessionId),
    )

    @Test
    fun testMapper() {
        // GIVEN
        val response = topStoryResponseOf()
        val playlistId = ""
        val playlistName = ""
        val playerAction = playerActionOf()

        // WHEN
        val mapped = mapper.map(response, userId, playlistId, playlistName, playerAction)

        // THEN
        assertEquals(mapped.playerSessionId.value, playerSessionId)
        assertEquals(mapped.id, playlistId)
        assertEquals(mapped.name, playlistName)
        assertEquals(mapped.playerAction, playerAction)
    }

    @Test
    fun testIfBurstAreMappedProperly() {
        // GIVEN
        val nullBurstResponse = topStoryResponseOf(bursts = null)
        val bursts = listOf(burstsResponseOf())
        val nonnullBurstResponse = topStoryResponseOf(bursts = bursts)

        // WHEN
        val mappedNullBurstResponse = mapper.map(nullBurstResponse, userId, "", "", playerActionOf())
        val mappedNonnullBurstResponse = mapper.map(nonnullBurstResponse, userId, "", "", playerActionOf())

        // THEN
        assertTrue(mappedNullBurstResponse.bursts.isEmpty())
        assertEquals(mappedNonnullBurstResponse.bursts.size, bursts.size)
    }

    @Test
    fun testIfQueryIsMappedProperly() {
        // GIVEN
        val nullQueryResponse = topStoryResponseOf(actualQuery = null)
        val actualQuery = ""
        val nonnullBurstResponse = topStoryResponseOf(actualQuery = actualQuery)

        // WHEN
        val mappedNullQueryResponse = mapper.map(nullQueryResponse, userId, "", "", playerActionOf())
        val mappedNonnullQueryResponse = mapper.map(nonnullBurstResponse, userId, "", "", playerActionOf())

        // THEN
        assertTrue(mappedNullQueryResponse.query.isEmpty())
        assertEquals(mappedNonnullQueryResponse.query, actualQuery)
    }
}

internal fun playerSessionIdGetterOf(playerSessionId: String = ""): PlayerSessionIdGetter =
    object : PlayerSessionIdGetter {
        override fun get(): PlayerSessionId = PlayerSessionId(playerSessionId)
    }
