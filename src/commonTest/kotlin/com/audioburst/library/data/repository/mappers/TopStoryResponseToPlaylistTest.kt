package com.audioburst.library.data.repository.mappers

import com.audioburst.library.interactors.playlistInfoOf
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

        // WHEN
        val mapped = mapper.map(response, userId, playlistInfoOf())

        // THEN
        assertEquals(mapped.playerSessionId.value, playerSessionId)
    }

    @Test
    fun testIfBurstAreMappedProperly() {
        // GIVEN
        val nullBurstResponse = topStoryResponseOf(bursts = null)
        val bursts = listOf(burstsResponseOf())
        val nonnullBurstResponse = topStoryResponseOf(bursts = bursts)

        // WHEN
        val mappedNullBurstResponse = mapper.map(nullBurstResponse, userId, playlistInfoOf())
        val mappedNonnullBurstResponse = mapper.map(nonnullBurstResponse, userId, playlistInfoOf())

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
        val mappedNullQueryResponse = mapper.map(nullQueryResponse, userId, playlistInfoOf())
        val mappedNonnullQueryResponse = mapper.map(nonnullBurstResponse, userId, playlistInfoOf())

        // THEN
        assertTrue(mappedNullQueryResponse.query.isEmpty())
        assertEquals(mappedNonnullQueryResponse.query, actualQuery)
    }
}

internal fun playerSessionIdGetterOf(playerSessionId: String = ""): PlayerSessionIdGetter =
    object : PlayerSessionIdGetter {
        override fun get(): PlayerSessionId = PlayerSessionId(playerSessionId)
    }
