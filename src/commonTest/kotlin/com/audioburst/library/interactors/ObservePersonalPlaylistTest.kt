package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.mappers.userStorageOf
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.*
import com.audioburst.library.runTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.test.assertEquals

class ObservePersonalPlaylistTest {

    private fun interactor(
        getPersonalPlaylistQueryId: Resource<PlaylistResult> = Resource.Data(playlistResultAsyncOf()),
        userStorage: UserStorage = userStorageOf(),
        requestPlaylistAsyncReturns: Flow<Result<PendingPlaylist>> = flowOf(),
    ): ObservePersonalPlaylist =
        ObservePersonalPlaylist(
            personalPlaylistRepository = personalPlaylistRepositoryOf(
                MockPersonalPlaylistRepository.Returns(getPersonalPlaylistQueryId = getPersonalPlaylistQueryId)
            ),
            userStorage = userStorage,
            requestPlaylistAsync = requestPlaylistAsyncOf(getPlaylistCall = requestPlaylistAsyncReturns),
        )

    @Test
    fun `test when userStorage returns zero selectedKeysCount`()= runTest {
        // GIVEN
        val selectedKeysCount = 0

        // WHEN
        val result = interactor(
            userStorage = userStorageOf(selectedKeysCount = selectedKeysCount)
        )()

        // THEN
        require(result.first() is Result.Error)
        assertEquals(LibraryError.NoKeysSelected, result.first().errorType)
    }

    @Test
    fun `test when requestPlaylistAsyncReturns returns flow with Error`()= runTest {
        // GIVEN
        val selectedKeysCount = 1
        val requestPlaylistAsyncReturns = flowOf(resultErrorOf())

        // WHEN
        val result = interactor(
            userStorage = userStorageOf(selectedKeysCount = selectedKeysCount),
            requestPlaylistAsyncReturns = requestPlaylistAsyncReturns
        )()

        // THEN
        require(result.first() is Result.Error)
    }

    @Test
    fun `test when requestPlaylistAsyncReturns returns flow with Data`()= runTest {
        // GIVEN
        val selectedKeysCount = 1
        val requestPlaylistAsyncReturns = flowOf(Result.Data(pendingPlaylistOf()))

        // WHEN
        val result = interactor(
            userStorage = userStorageOf(selectedKeysCount = selectedKeysCount),
            requestPlaylistAsyncReturns = requestPlaylistAsyncReturns
        )()

        // THEN
        require(result.first() is Result.Data)
    }
}
