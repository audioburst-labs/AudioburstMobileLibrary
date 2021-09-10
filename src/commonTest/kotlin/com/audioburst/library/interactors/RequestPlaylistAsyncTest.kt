package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.storage.InMemoryPlaylistStorage
import com.audioburst.library.data.storage.PlaylistStorage
import com.audioburst.library.models.*
import com.audioburst.library.runTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RequestPlaylistAsyncTest {

    private val asyncCallInterval: Duration = 1.0.toDuration(DurationUnit.Milliseconds)
    private fun interactor(
        getPersonalPlaylistReturns: List<Resource<PendingPlaylist>> = emptyList(),
        getUserReturns: Resource<User>,
        postContentLoadEvent: PostContentLoadEvent = postContentLoadEventOf(),
        playlistStorage: PlaylistStorage = InMemoryPlaylistStorage()
    ): RequestPlaylistAsyncInteractor =
        RequestPlaylistAsyncInteractor(
            personalPlaylistRepository = personalPlaylistRepositoryOf(
                MockPersonalPlaylistRepository.Returns(
                    getPersonalPlaylist = getPersonalPlaylistReturns,
                )
            ),
            postContentLoadEvent = postContentLoadEvent,
            getUser = getUserOf(getUserReturns),
            playlistStorage = playlistStorage,
            asyncCallInterval = asyncCallInterval,
        )

    @Test
    fun `test when GetUser returns Error`()= runTest {
        // GIVEN
        val getUserReturns = resourceErrorOf()
        val getPlaylistCallReturns = Resource.Data(playlistResultAsyncOf())

        // WHEN
        val result = interactor(getUserReturns = getUserReturns)() { getPlaylistCallReturns }

        // THEN
        assertTrue(result.first() is Result.Error)
    }

    @Test
    fun `test when GetUser returns Data and getPlaylistCall returns Error`()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPlaylistCallReturns = resourceErrorOf()

        // WHEN
        val result = interactor(getUserReturns = getUserReturns)() { getPlaylistCallReturns }

        // THEN
        assertTrue(result.first() is Result.Error)
    }

    @Test
    fun `test that ready PendingPlaylist is returned when getPlaylistCall returns Finished PlaylistResult`()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val playlist = playlistOf()
        val getPlaylistCallReturns = Resource.Data(playlistResultFinishedOf(playlist))

        // WHEN
        val result = interactor(getUserReturns = getUserReturns)() { getPlaylistCallReturns }

        // THEN
        val first = result.first()
        require(first is Result.Data)
        assertTrue(first.value.isReady)
        assertEquals(playlist, first.value.playlist)
    }

    @Test
    fun `test that Error is returned when getPlaylistCallReturns Async and getPersonalPlaylistReturns returns Error`()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPlaylistCallReturns = Resource.Data(playlistResultAsyncOf())
        val getPersonalPlaylistReturns = listOf(resourceErrorOf())

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylistReturns = getPersonalPlaylistReturns,
        )() { getPlaylistCallReturns }

        // THEN
        assertTrue(result.first() is Result.Error)
    }

    @Test
    fun `test that ready PendingPlaylist is returned when getPlaylistCallReturns Async and getPersonalPlaylistReturns returns Data`()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPlaylistCallReturns = Resource.Data(playlistResultAsyncOf())
        val playlist = playlistOf()
        val getPersonalPlaylistReturns = listOf(
            Resource.Data(
                pendingPlaylistOf(
                    isReady = true,
                    playlist = playlist,
                )
            )
        )

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylistReturns = getPersonalPlaylistReturns,
        )() { getPlaylistCallReturns }

        // THEN
        val first = result.first()
        require(first is Result.Data)
        assertTrue(first.value.isReady)
        assertEquals(playlist, first.value.playlist)
    }

    @Test
    fun `test that emission is completed when there is an Error in the middle`()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPlaylistCallReturns = Resource.Data(playlistResultAsyncOf())
        val getPersonalPlaylistReturns = listOf(
            Resource.Data(pendingPlaylistOf(isReady = false)),
            resourceErrorOf(),
            Resource.Data(pendingPlaylistOf(isReady = false)),
            Resource.Data(pendingPlaylistOf(isReady = false)),
            Resource.Data(pendingPlaylistOf(isReady = true)),
        )

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylistReturns = getPersonalPlaylistReturns,
        )() { getPlaylistCallReturns }

        // THEN
        val values = result.toList()
        assertTrue(values.size == 2)
        assertTrue(values.last() is Result.Error)
    }

    @Test
    fun `test that there are only two emissions when getPersonalPlaylistReturns keeps returning the same playlist`()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPlaylistCallReturns = Resource.Data(playlistResultAsyncOf())
        val playlist = playlistOf(bursts = (0..2).map { burstOf(id = "id$it") })
        val getPersonalPlaylistReturns = listOf(
            Resource.Data(pendingPlaylistOf(isReady = false, playlist = playlist)),
            Resource.Data(pendingPlaylistOf(isReady = false, playlist = playlist)),
            Resource.Data(pendingPlaylistOf(isReady = false, playlist = playlist)),
            Resource.Data(pendingPlaylistOf(isReady = false, playlist = playlist)),
            Resource.Data(pendingPlaylistOf(isReady = false, playlist = playlist)),
            Resource.Data(pendingPlaylistOf(isReady = true, playlist = playlist)),
        )

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylistReturns = getPersonalPlaylistReturns,
        )() { getPlaylistCallReturns }

        // THEN
        val values = result.toList()
        assertTrue(values.size == 2)
    }

    @Test
    fun `test that only PendingPlaylists with unique Bursts are emitted`()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPlaylistCallReturns = Resource.Data(playlistResultAsyncOf())
        val getPersonalPlaylistReturns = listOf(
            Resource.Data(pendingPlaylistOf(isReady = false, playlist = playlistOf(bursts = listOf(burstOf(id = "id1"))))),
            Resource.Data(pendingPlaylistOf(isReady = false, playlist = playlistOf(bursts = listOf(burstOf(id = "id1"))))),
            Resource.Data(pendingPlaylistOf(isReady = false, playlist = playlistOf(bursts = listOf(burstOf(id = "id1"), burstOf(id = "id2"))))),
            Resource.Data(pendingPlaylistOf(isReady = false, playlist = playlistOf(bursts = listOf(burstOf(id = "id1"), burstOf(id = "id2"))))),
            Resource.Data(pendingPlaylistOf(isReady = false, playlist = playlistOf(bursts = listOf(burstOf(id = "id1"), burstOf(id = "id2"), burstOf(id = "id3"))))),
            Resource.Data(pendingPlaylistOf(isReady = false, playlist = playlistOf(bursts = listOf(burstOf(id = "id1"), burstOf(id = "id2"), burstOf(id = "id3"))))),
            Resource.Data(pendingPlaylistOf(isReady = true, playlist = playlistOf(bursts = listOf(burstOf(id = "id1"))))),
        )

        // WHEN
        val result = interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylistReturns = getPersonalPlaylistReturns,
        )() { getPlaylistCallReturns }

        // THEN
        val values = result.toList()
        assertTrue(values.size == 4)
    }

    @Test
    fun `test that ContentLoad event is getting sent when there is a ready PendingPlaylist with at least one Burst`()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPlaylistCallReturns = Resource.Data(playlistResultAsyncOf())
        val getPersonalPlaylistReturns = listOf(
            Resource.Data(
                pendingPlaylistOf(
                    isReady = true,
                    playlist = playlistOf(bursts = listOf(burstOf()))
                )
            )
        )
        val playbackEventHandler = MemorablePlaybackEventHandler()

        // WHEN
        interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylistReturns = getPersonalPlaylistReturns,
            postContentLoadEvent = postContentLoadEventOf(
                playbackEventHandler = playbackEventHandler
            ),
        )() { getPlaylistCallReturns }.toList()

        // THEN
        assertTrue(playbackEventHandler.sentEvents.size == 1)
    }

    @Test
    fun `test that Playlist is getting saved in PlaylistStorage`()= runTest {
        // GIVEN
        val getUserReturns = Resource.Data(userOf())
        val getPlaylistCallReturns = Resource.Data(playlistResultAsyncOf())
        val playlist = playlistOf(bursts = listOf(burstOf()))
        val getPersonalPlaylistReturns = listOf(
            Resource.Data(
                pendingPlaylistOf(
                    isReady = true,
                    playlist = playlist,
                )
            )
        )
        val playlistStorage = InMemoryPlaylistStorage()

        // WHEN
        interactor(
            getUserReturns = getUserReturns,
            getPersonalPlaylistReturns = getPersonalPlaylistReturns,
            playlistStorage = playlistStorage,
        )() { getPlaylistCallReturns }.toList()

        // THEN
        assertEquals(playlist, playlistStorage.currentPlaylist)
    }
}
