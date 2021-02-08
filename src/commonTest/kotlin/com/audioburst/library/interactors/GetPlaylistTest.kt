package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.Result
import com.audioburst.library.models.User
import com.audioburst.library.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class GetPlaylistTest {

    private fun interactor(
        getPlaylistByPlaylistInfo: Resource<Playlist> = Resource.Data(playlistOf()),
        getPlaylistByByteArray: Resource<Playlist> = Resource.Data(playlistOf()),
        userResource: Resource<User>,
        postContentLoadEvent: PostContentLoadEvent = postContentLoadEventOf()
    ): GetPlaylist =
        GetPlaylist(
            getUser = getUserOf(userResource),
            userRepository = userRepositoryOf(
                returns = MockUserRepository.Returns(
                    getPlaylistByPlaylistInfo = getPlaylistByPlaylistInfo,
                    getPlaylistByByteArray = getPlaylistByByteArray,
                )
            ),
            postContentLoadEvent = postContentLoadEvent,
        )

    @Test
    fun testIfResultDataIsReturnedWhenGetPlaylistWithPlaylistInfoReturnsThat() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf())
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            getPlaylistByPlaylistInfo = getPlaylistReturn,
            userResource = userResource,
        )(playlistInfoOf())

        // THEN
        assertTrue(resource is Result.Data)
    }

    @Test
    fun testIfResultDataIsReturnedWhenGetPlaylistWithByteArrayReturnsThat() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf())
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            getPlaylistByByteArray = getPlaylistReturn,
            userResource = userResource,
        )(byteArrayOf())

        // THEN
        assertTrue(resource is Result.Data)
    }

    @Test
    fun testIfGetUserReturnsErrorThenErrorIsReturnedGetPlaylistWithPlaylistInfoIsCalled() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf())
        val userResource = resourceErrorOf()

        // WHEN
        val resource = interactor(
            getPlaylistByPlaylistInfo = getPlaylistReturn,
            userResource = userResource,
        )(playlistInfoOf())

        // THEN
        assertTrue(resource is Result.Error)
    }

    @Test
    fun testIfGetUserReturnsErrorThenErrorIsReturnedGetPlaylistWithByteArrayIsCalled() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf())
        val userResource = resourceErrorOf()

        // WHEN
        val resource = interactor(
            getPlaylistByByteArray = getPlaylistReturn,
            userResource = userResource,
        )(byteArrayOf())

        // THEN
        assertTrue(resource is Result.Error)
    }

    @Test
    fun testIfGetUserReturnsUserAndUserRepositoryGetPlaylistWithPlaylistTypeReturnsErrorThenErrorIsReturned() = runTest {
        // GIVEN
        val getPlaylistReturn = resourceErrorOf()
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            getPlaylistByPlaylistInfo = getPlaylistReturn,
            userResource = userResource,
        )(playlistInfoOf())

        // THEN
        assertTrue(resource is Result.Error)
    }

    @Test
    fun testIfGetUserReturnsUserAndUserRepositoryGetPlaylistWithByteArrayReturnsErrorThenErrorIsReturned() = runTest {
        // GIVEN
        val getPlaylistReturn = resourceErrorOf()
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            getPlaylistByByteArray = getPlaylistReturn,
            userResource = userResource,
        )(byteArrayOf())

        // THEN
        assertTrue(resource is Result.Error)
    }

    @Test
    fun testIfContentLoadEventIsGettingSentWhenGetPlaylistWithPlaylistTypeIsSuccessful() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf(bursts = listOf(burstOf())))
        val userResource = Resource.Data(userOf())
        val playbackEventHandler = MemorablePlaybackEventHandler()

        // WHEN
        interactor(
            getPlaylistByPlaylistInfo = getPlaylistReturn,
            userResource = userResource,
            postContentLoadEvent = postContentLoadEventOf(
                playbackEventHandler = playbackEventHandler
            )
        )(playlistInfoOf())

        // THEN
        assertTrue(playbackEventHandler.sentEvents.isNotEmpty())
    }

    @Test
    fun testIfContentLoadEventIsGettingSentWhenGetPlaylistWithByteArrayIsSuccessful() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf(bursts = listOf(burstOf())))
        val userResource = Resource.Data(userOf())
        val playbackEventHandler = MemorablePlaybackEventHandler()

        // WHEN
        interactor(
            getPlaylistByByteArray = getPlaylistReturn,
            userResource = userResource,
            postContentLoadEvent = postContentLoadEventOf(
                playbackEventHandler = playbackEventHandler
            )
        )(byteArrayOf())

        // THEN
        assertTrue(playbackEventHandler.sentEvents.isNotEmpty())
    }
}
