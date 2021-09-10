package com.audioburst.library.data.repository.mappers

import com.audioburst.library.interactors.playerSessionIdOf
import com.audioburst.library.models.PlaylistResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TopStoryResponseToPlaylistResultTest {

    private val playerSessionId = playerSessionIdOf()
    private val userId = ""
    private val playerAction = playerActionOf()
    private val mapper = TopStoryResponseToPlaylistResult(topStoryResponseToPlaylist = topStoryResponseToPlaylistOf())

    @Test
    fun `test that Async is returned when type is async`() {
        // GIVEN
        val topStoryResponse = topStoryResponseOf(type = "async")

        // WHEN
        val mapped = mapper.map(topStoryResponse, userId, playerSessionId, playerAction)

        // THEN
        assertTrue(mapped is PlaylistResult.Async)
    }

    @Test
    fun `test that Finished is returned when type is null`() {
        // GIVEN
        val topStoryResponse = topStoryResponseOf(type = null)

        // WHEN
        val mapped = mapper.map(topStoryResponse, userId, playerSessionId, playerAction)

        // THEN
        assertTrue(mapped is PlaylistResult.Finished)
    }

    @Test
    fun `test that Finished is returned when type is playlist`() {
        // GIVEN
        val topStoryResponse = topStoryResponseOf(type = "playlist")

        // WHEN
        val mapped = mapper.map(topStoryResponse, userId, playerSessionId, playerAction)

        // THEN
        assertTrue(mapped is PlaylistResult.Finished)
    }

    @Test
    fun `test that Async has correct values`() {
        // GIVEN
        val queryId = 1L
        val topStoryResponse = topStoryResponseOf(type = "async", queryID = queryId)

        // WHEN
        val mapped = mapper.map(topStoryResponse, userId, playerSessionId, playerAction)

        // THEN
        assertTrue(mapped is PlaylistResult.Async)
        assertEquals(playerAction, mapped.playerAction)
        assertEquals(playerSessionId, mapped.playerSessionId)
        assertEquals(queryId, mapped.queryId.value)
    }
}