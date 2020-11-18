package com.audioburst.library.data.repository.mappers

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TopStoryResponseToPendingPlaylistTest {

    private val playerSessionId: String = ""
    private val userId = ""

    private val mapper = TopStoryResponseToPendingPlaylist(
        topStoryResponseToPlaylist = TopStoryResponseToPlaylist(
            burstResponseToBurstMapper = BurstResponseToBurstMapper(
                libraryConfiguration = libraryConfigurationOf(),
                sourceResponseToBurstSourceMapper = SourceResponseToBurstSourceMapper(),
            ),
            playerSessionIdGetter = playerSessionIdGetterOf(playerSessionId = playerSessionId),
        )
    )

    @Test
    fun testIfPlaylistNameIsEqualToQueryWhenActualQueryIsNull() {
        // GIVEN
        val query = "query"
        val response = topStoryResponseOf(
            actualQuery = null,
            query = query
        )

        // WHEN
        val mapped = mapper.map(response, userId)

        // THEN
        assertEquals(mapped.playlist.name, query)
    }

    @Test
    fun testIfPlaylistNameIsEqualToActualQueryIfItIsNotNull() {
        // GIVEN
        val actualQuery = "actualQuery"
        val response = topStoryResponseOf(actualQuery = actualQuery)

        // WHEN
        val mapped = mapper.map(response, userId)

        // THEN
        assertEquals(mapped.playlist.name, actualQuery)
    }

    @Test
    fun testIfPlaylistNameIsEqualToEmptyStringWhenBothQueryAndActualQueryAreNull() {
        // GIVEN
        val response = topStoryResponseOf(
            actualQuery = null,
            query = null
        )

        // WHEN
        val mapped = mapper.map(response, userId)

        // THEN
        assertEquals(mapped.playlist.name, "")
    }

    @Test
    fun testIfPlaylistIsIsEqualToUserId() {
        // GIVEN
        val response = topStoryResponseOf()

        // WHEN
        val mapped = mapper.map(response, userId)

        // THEN
        assertEquals(mapped.playlist.id, userId)
    }

    @Test
    fun testIfPendingPlaylistIsReadyWhenMessageIsEqualToDONE() {
        // GIVEN
        val response = topStoryResponseOf(
            message = "DONE"
        )

        // WHEN
        val mapped = mapper.map(response, userId)

        // THEN
        assertTrue(mapped.isReady)
    }

    @Test
    fun testIfPendingPlaylistIsNotReadyWhenMessageIsNull() {
        // GIVEN
        val response = topStoryResponseOf(
            message = null
        )

        // WHEN
        val mapped = mapper.map(response, userId)

        // THEN
        assertTrue(!mapped.isReady)
    }

    @Test
    fun testIfPendingPlaylistIsNotReadyWhenMessageDifferentThanDONE() {
        // GIVEN
        val response = topStoryResponseOf(
            message = ""
        )

        // WHEN
        val mapped = mapper.map(response, userId)

        // THEN
        assertTrue(!mapped.isReady)
    }
}
