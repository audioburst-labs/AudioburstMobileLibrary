package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.models.LibraryError
import com.audioburst.library.models.PendingPlaylist
import com.audioburst.library.models.PlaylistResult
import com.audioburst.library.models.Result
import com.audioburst.library.runTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchTest {

    private fun interactor(
        searchByQuery: Resource<PlaylistResult> = Resource.Data(playlistResultFinishedOf()),
        searchByByteArray: Resource<PlaylistResult> = Resource.Data(playlistResultFinishedOf()),
        requestPlaylistAsyncReturns: Flow<Result<PendingPlaylist>> = flowOf(),
    ): Search =
        Search(
            playlistRepository = playlistRepositoryOf(
                returns = MockPlaylistRepository.Returns(
                    searchByQuery = searchByQuery,
                    searchByByteArray = searchByByteArray,
                )
            ),
            requestPlaylistAsync = requestPlaylistAsyncOf(getPlaylistCall = requestPlaylistAsyncReturns),
        )

    @Test
    fun `test that Error is returned when requestPlaylistAsyncReturns returns Error when searching with query`() = runTest {
        // GIVEN
        val requestPlaylistAsyncReturns = flowOf(resultErrorOf())

        // WHEN
        val result = interactor(requestPlaylistAsyncReturns = requestPlaylistAsyncReturns)("")

        // THEN
        assertTrue(result.first() is Result.Error)
    }

    @Test
    fun `test that Error is returned when requestPlaylistAsyncReturns returns Error when searching with byteArray`() = runTest {
        // GIVEN
        val requestPlaylistAsyncReturns = flowOf(resultErrorOf())

        // WHEN
        val result = interactor(requestPlaylistAsyncReturns = requestPlaylistAsyncReturns)(byteArrayOf())

        // THEN
        assertTrue(result.first() is Result.Error)
    }

    @Test
    fun `test that NoSearchResults is returned when requestPlaylistAsyncReturns returns empty Playlist when searching with byteArray`() = runTest {
        // GIVEN
        val requestPlaylistAsyncReturns = flowOf(Result.Data(pendingPlaylistOf(isReady = true, playlist = playlistOf(bursts = emptyList()))))

        // WHEN
        val result = interactor(requestPlaylistAsyncReturns = requestPlaylistAsyncReturns)(byteArrayOf())

        // THEN
        val first = result.first()
        assertTrue(first is Result.Error)
        assertEquals(LibraryError.NoSearchResults, first.error)
    }

    @Test
    fun `test that NoSearchResults is returned when requestPlaylistAsyncReturns returns empty Playlist when searching with query`() = runTest {
        // GIVEN
        val requestPlaylistAsyncReturns = flowOf(Result.Data(pendingPlaylistOf(isReady = true, playlist = playlistOf(bursts = emptyList()))))

        // WHEN
        val result = interactor(requestPlaylistAsyncReturns = requestPlaylistAsyncReturns)("")

        // THEN
        val first = result.first()
        assertTrue(first is Result.Error)
        assertEquals(LibraryError.NoSearchResults, first.error)
    }

    @Test
    fun `test that PendingPlaylist is returned when requestPlaylistAsyncReturns returns not empty Playlist when searching with query`() = runTest {
        // GIVEN
        val requestPlaylistAsyncReturns = flowOf(
            Result.Data(
                pendingPlaylistOf(
                    isReady = true,
                    playlist = playlistOf(
                        bursts = listOf(burstOf())
                    )
                )
            )
        )

        // WHEN
        val result = interactor(requestPlaylistAsyncReturns = requestPlaylistAsyncReturns)("")

        // THEN
        assertTrue(result.first() is Result.Data)
    }

    @Test
    fun `test that PendingPlaylist is returned when requestPlaylistAsyncReturns returns not empty Playlist when searching with byteArray`() = runTest {
        // GIVEN
        val requestPlaylistAsyncReturns = flowOf(
            Result.Data(
                pendingPlaylistOf(
                    isReady = true,
                    playlist = playlistOf(
                        bursts = listOf(burstOf())
                    )
                )
            )
        )

        // WHEN
        val result = interactor(requestPlaylistAsyncReturns = requestPlaylistAsyncReturns)(byteArrayOf())

        // THEN
        assertTrue(result.first() is Result.Data)
    }
}
