package com.audioburst.library.data.repository.mappers

import com.audioburst.library.utils.LibraryConfiguration
import com.audioburst.library.utils.PlayerSessionIdGetter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TopStoryResponseToPendingPlaylistTest {

    private val playerSessionId: String = ""
    private val userId = ""

    private val mapper = topStoryResponseToPendingPlaylistOf(
        topStoryResponseToPlaylist = topStoryResponseToPlaylistOf(
            playerSessionIdGetter = playerSessionIdGetterOf(
                playerSessionId = playerSessionId,
            )
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
    fun testIfPlaylistIdIsIsEqualToQueryId() {
        // GIVEN
        val queryId = 1L
        val response = topStoryResponseOf(queryID = queryId)

        // WHEN
        val mapped = mapper.map(response, userId)

        // THEN
        assertEquals(mapped.playlist.id, queryId.toString())
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

internal fun topStoryResponseToPendingPlaylistOf(
    topStoryResponseToPlaylist: TopStoryResponseToPlaylist = topStoryResponseToPlaylistOf()
): TopStoryResponseToPendingPlaylist =
    TopStoryResponseToPendingPlaylist(topStoryResponseToPlaylist = topStoryResponseToPlaylist,)

internal fun topStoryResponseToPlaylistOf(
    burstResponseToBurstMapper: BurstResponseToBurstMapper = burstResponseToBurstMapperOf(),
    playerSessionIdGetter: PlayerSessionIdGetter = playerSessionIdGetterOf(),
): TopStoryResponseToPlaylist =
    TopStoryResponseToPlaylist(
        burstResponseToBurstMapper = burstResponseToBurstMapper,
        playerSessionIdGetter = playerSessionIdGetter,
    )

internal fun burstResponseToBurstMapperOf(
    libraryConfiguration: LibraryConfiguration = libraryConfigurationOf(),
    sourceResponseToBurstSourceMapper: SourceResponseToBurstSourceMapper = sourceResponseToBurstSourceMapperOf(),
): BurstResponseToBurstMapper =
    BurstResponseToBurstMapper(
        libraryConfiguration = libraryConfiguration,
        sourceResponseToBurstSourceMapper = sourceResponseToBurstSourceMapper,
    )

internal fun sourceResponseToBurstSourceMapperOf(): SourceResponseToBurstSourceMapper = SourceResponseToBurstSourceMapper()
