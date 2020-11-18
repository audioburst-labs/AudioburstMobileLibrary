package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.models.PendingPlaylist
import com.audioburst.library.models.PersonalPlaylistQueryId
import com.audioburst.library.models.Result
import com.audioburst.library.models.User
import com.audioburst.library.runTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlin.test.Test
import kotlin.test.assertTrue

class ObservePersonalPlaylistTest {

    private fun interactor(
        getPersonalPlaylistQueryId: Resource<PersonalPlaylistQueryId>,
        getPersonalPlaylist: List<Resource<PendingPlaylist>>,
        getUserReturns: Resource<User>,
    ): ObservePersonalPlaylist =
        ObservePersonalPlaylist(
            getUser = getUserOf(getUserReturns),
            personalPlaylistRepository = personalPlaylistRepositoryOf(
                MockPersonalPlaylistRepository.Returns(
                    getPersonalPlaylistQueryId = getPersonalPlaylistQueryId,
                    getPersonalPlaylist = getPersonalPlaylist,
                )
            )
        )

    @Test
    fun testWhenGetUserReturnsError()= runTest {
        // GIVEN
        val getUserReturns = resourceErrorOf()
        val getPersonalPlaylistQueryId = Resource.Data(personalPlaylistQueryIdOf())
        val getPersonalPlaylist = listOf(Resource.Data(pendingPlaylistOf()))

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylist = getPersonalPlaylist,
            getPersonalPlaylistQueryId = getPersonalPlaylistQueryId
        )()

        // THEN
        assertTrue(result.first() is Result.Error)
    }

    @Test
    fun testWhenGetUserReturnsDataAndGetPersonalPlaylistQueryIdReturnsError()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPersonalPlaylistQueryId = resourceErrorOf()
        val getPersonalPlaylist = listOf(Resource.Data(pendingPlaylistOf()))

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylist = getPersonalPlaylist,
            getPersonalPlaylistQueryId = getPersonalPlaylistQueryId
        )()

        // THEN
        assertTrue(result.first() is Result.Error)
    }

    @Test
    fun testWhenGetPersonalPlaylistReturnsReadyPlaylist()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPersonalPlaylistQueryId = Resource.Data(personalPlaylistQueryIdOf())
        val playlist = pendingPlaylistOf(isReady = true)
        val getPersonalPlaylist = listOf(Resource.Data(playlist))

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylist = getPersonalPlaylist,
            getPersonalPlaylistQueryId = getPersonalPlaylistQueryId
        )()

        // THEN
        assertTrue(result.first() is Result.Data)
    }

    @Test
    fun testIfEmissionIsCompletedWhenThereIsAnErrorInTheMiddle()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPersonalPlaylistQueryId = Resource.Data(personalPlaylistQueryIdOf())
        val getPersonalPlaylist = listOf(
            Resource.Data(pendingPlaylistOf(isReady = false)),
            resourceErrorOf(),
            Resource.Data(pendingPlaylistOf(isReady = false)),
            Resource.Data(pendingPlaylistOf(isReady = false)),
            Resource.Data(pendingPlaylistOf(isReady = true)),
        )

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylist = getPersonalPlaylist,
            getPersonalPlaylistQueryId = getPersonalPlaylistQueryId
        )()

        // THEN
        val values = result.toList()
        assertTrue(values.size == 2)
        assertTrue(values.last() is Result.Error)
    }

    @Test
    fun testIfEmissionIsCompletedWhenThereIsAReadyPlaylistInTheMiddle()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPersonalPlaylistQueryId = Resource.Data(personalPlaylistQueryIdOf())
        val getPersonalPlaylist = listOf(
            Resource.Data(pendingPlaylistOf(isReady = false)),
            Resource.Data(pendingPlaylistOf(isReady = true)),
            Resource.Data(pendingPlaylistOf(isReady = false)),
            Resource.Data(pendingPlaylistOf(isReady = false)),
        )

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylist = getPersonalPlaylist,
            getPersonalPlaylistQueryId = getPersonalPlaylistQueryId
        )()

        // THEN
        val values = result.toList()
        assertTrue(values.size == 2)
        assertTrue(values.last() is Result.Data)
    }

    @Test
    fun testWhenGetPersonalPlaylistKeepsReturnTheSamePlaylistTheEmittedValueIsOnlyOne()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPersonalPlaylistQueryId = Resource.Data(personalPlaylistQueryIdOf())
        val getPersonalPlaylist = listOf(
            Resource.Data(pendingPlaylistOf(isReady = false)),
            Resource.Data(pendingPlaylistOf(isReady = false)),
            Resource.Data(pendingPlaylistOf(isReady = true)),
        )

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylist = getPersonalPlaylist,
            getPersonalPlaylistQueryId = getPersonalPlaylistQueryId
        )()

        // THEN
        assertTrue(result.toList().size == 2)
    }

    @Test
    fun testIfOnlyUniqueBurstsAreEmitted()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPersonalPlaylistQueryId = Resource.Data(personalPlaylistQueryIdOf())
        val getPersonalPlaylist = listOf(
            Resource.Data(pendingPlaylistOf(isReady = false, playlistOf(bursts = listOf(burstOf(id = "id1"))))),
            Resource.Data(pendingPlaylistOf(isReady = false, playlistOf(bursts = listOf(burstOf(id = "id1"))))),
            Resource.Data(pendingPlaylistOf(isReady = true, playlistOf(bursts = listOf(burstOf(id = "id1"), burstOf(id = "id2"))))),
        )

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylist = getPersonalPlaylist,
            getPersonalPlaylistQueryId = getPersonalPlaylistQueryId
        )()

        // THEN
        assertTrue(result.toList().size == 2)
    }
}
