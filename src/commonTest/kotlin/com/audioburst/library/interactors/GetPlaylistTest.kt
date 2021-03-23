package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.mappers.userStorageOf
import com.audioburst.library.data.storage.InMemoryPlaylistStorage
import com.audioburst.library.data.storage.ListenedBurstStorage
import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.Result
import com.audioburst.library.models.User
import com.audioburst.library.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPlaylistTest {

    private val playlistStorage = InMemoryPlaylistStorage()
    private fun interactor(
        getPlaylistByPlaylistInfo: Resource<Playlist> = Resource.Data(playlistOf()),
        getPlaylistByByteArray: Resource<Playlist> = Resource.Data(playlistOf()),
        userResource: Resource<User>,
        postContentLoadEvent: PostContentLoadEvent = postContentLoadEventOf(),
        listenedBurstStorage: ListenedBurstStorage = listenedBurstsStorageOf(),
        userStorage: UserStorage = userStorageOf(),
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
            playlistStorage = playlistStorage,
            listenedBurstStorage = listenedBurstStorage,
            userStorage = userStorage,
        )

    @AfterTest
    fun clear() {
        playlistStorage.clear()
    }

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
        assertTrue(playlistStorage.currentPlaylist != null)
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
        assertTrue(playlistStorage.currentPlaylist != null)
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

    private fun testBurstFiltering(
        isFilteringApplied: Boolean,
        interactorCall: suspend (
            getPlaylistReturn: Resource<Playlist>,
            userResource: Resource<User>,
            listenedBurstStorage: ListenedBurstStorage
        ) -> Result<Playlist>
    ) = runTest {
        // GIVEN
        val listenedBurstIds = listOf("id1", "id2", "id3")
        val notListenedBurstId = "id4"
        val getPlaylistReturn = Resource.Data(
            playlistOf(bursts = (listenedBurstIds + listOf(notListenedBurstId)).map { burstOf(id = it) })
        )
        val listenedBursts = listenedBurstIds.map { listenedBurstOf(id = it) }
        val userResource = Resource.Data(userOf())
        val listenedBurstStorage = listenedBurstsStorageOf(
            getAll = listenedBursts,
        )

        // WHEN
        val resource = interactorCall(getPlaylistReturn, userResource, listenedBurstStorage)

        // THEN
        assertTrue(resource is Result.Data)
        if (isFilteringApplied) {
            assertTrue(resource.value.bursts.size == 1)
            assertEquals(notListenedBurstId, resource.value.bursts.first().id)
        } else {
            assertEquals(getPlaylistReturn.result.bursts.size, resource.value.bursts.size)
        }
    }

    @Test
    fun testIfBurstsAreGettingFilteredAccordingToWhatAlreadyHasBeenListenedToWhenRequestingPlaylistByPlaylistInfo() {
        testBurstFiltering(isFilteringApplied = true) { getPlaylistByPlaylistInfo, userResource, listenedBurstStorage ->
            interactor(
                getPlaylistByPlaylistInfo = getPlaylistByPlaylistInfo,
                userResource = userResource,
                listenedBurstStorage = listenedBurstStorage,
                userStorage = userStorageOf(filterListenedBursts = true)
            )(playlistInfoOf())
        }
    }

    @Test
    fun testIfBurstsAreNotGettingFilteredAccordingToWhatAlreadyHasBeenListenedToWhenRequestingPlaylistByPlaylistInfoWhenFilterListenedBurstsIsFalse() {
        testBurstFiltering(isFilteringApplied = false) { getPlaylistByPlaylistInfo, userResource, listenedBurstStorage ->
            interactor(
                getPlaylistByPlaylistInfo = getPlaylistByPlaylistInfo,
                userResource = userResource,
                listenedBurstStorage = listenedBurstStorage,
                userStorage = userStorageOf(filterListenedBursts = false)
            )(playlistInfoOf())
        }
    }

    @Test
    fun testIfBurstsAreGettingFilteredAccordingToWhatAlreadyHasBeenListenedToWhenRequestingPlaylistByByteArray() {
        testBurstFiltering(isFilteringApplied = true) { getPlaylistByPlaylistInfo, userResource, listenedBurstStorage ->
            interactor(
                getPlaylistByByteArray = getPlaylistByPlaylistInfo,
                userResource = userResource,
                listenedBurstStorage = listenedBurstStorage,
                userStorage = userStorageOf(filterListenedBursts = true)
            )(byteArrayOf())
        }
    }

    @Test
    fun testIfBurstsAreNotGettingFilteredAccordingToWhatAlreadyHasBeenListenedToWhenRequestingPlaylistByByteArrayWhenFilterListenedBurstsIsFalse() {
        testBurstFiltering(isFilteringApplied = false) { getPlaylistByPlaylistInfo, userResource, listenedBurstStorage ->
            interactor(
                getPlaylistByByteArray = getPlaylistByPlaylistInfo,
                userResource = userResource,
                listenedBurstStorage = listenedBurstStorage,
                userStorage = userStorageOf(filterListenedBursts = false)
            )(byteArrayOf())
        }
    }
}
