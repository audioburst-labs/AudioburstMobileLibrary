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
        channelReturns: Resource<Playlist> = Resource.Data(playlistOf()),
        userGeneratedReturns: Resource<Playlist> = Resource.Data(playlistOf()),
        sourceReturns: Resource<Playlist> = Resource.Data(playlistOf()),
        accountReturns: Resource<Playlist> = Resource.Data(playlistOf()),
        userRepositoryReturns: Resource<User>,
        postContentLoadEvent: PostContentLoadEvent = postContentLoadEventOf(),
        listenedBurstStorage: ListenedBurstStorage = listenedBurstsStorageOf(),
        userStorage: UserStorage = userStorageOf(),
    ): GetPlaylist =
        GetPlaylist(
            getUser = getUserOf(userRepositoryReturns),
            playlistRepository = playlistRepositoryOf(
                returns = MockPlaylistRepository.Returns(
                    getPlaylistByPlaylistInfo = getPlaylistByPlaylistInfo,
                    channel = channelReturns,
                    userGenerated = userGeneratedReturns,
                    source = sourceReturns,
                    account = accountReturns,
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
            userRepositoryReturns = userResource,
        )(playlistInfoOf())

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
            userRepositoryReturns = userResource,
        )(playlistInfoOf())

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
            userRepositoryReturns = userResource,
        )(playlistInfoOf())

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
            userRepositoryReturns = userResource,
            postContentLoadEvent = postContentLoadEventOf(
                playbackEventHandler = playbackEventHandler
            )
        )(playlistInfoOf())

        // THEN
        assertTrue(playbackEventHandler.sentEvents.isNotEmpty())
    }

    @Test
    fun testIfBurstsAreGettingFilteredAccordingToWhatAlreadyHasBeenListenedToWhenRequestingPlaylistByPlaylistInfo() = runTest {
        // GIVEN
        val listenedBurstIds = listOf("id1", "id2", "id3")
        val notListenedBurstId = "id4"
        val getPlaylistReturn = Resource.Data(
            playlistOf(
                intent = Playlist.Intent.Playlists,
                bursts = (listenedBurstIds + listOf(notListenedBurstId)).map { burstOf(id = it) }
            )
        )
        val listenedBursts = listenedBurstIds.map { listenedBurstOf(id = it) }
        val userResource = Resource.Data(userOf())
        val listenedBurstStorage = listenedBurstsStorageOf(
            getAll = listenedBursts,
        )

        // WHEN
        val resource = interactor(
            getPlaylistByPlaylistInfo = getPlaylistReturn,
            userRepositoryReturns = userResource,
            listenedBurstStorage = listenedBurstStorage,
            userStorage = userStorageOf(filterListenedBursts = true)
        )(playlistInfoOf())

        // THEN
        assertTrue(resource is Result.Data)
        assertTrue(resource.value.bursts.size == 1)
        assertEquals(notListenedBurstId, resource.value.bursts.first().id)
    }

    @Test
    fun testIfBurstsAreNotGettingFilteredAccordingToWhatAlreadyHasBeenListenedToWhenRequestingPlaylistByPlaylistInfoWhenFilterListenedBurstsIsFalse() = runTest {
        // GIVEN
        val listenedBurstIds = listOf("id1", "id2", "id3")
        val notListenedBurstId = "id4"
        val getPlaylistReturn = Resource.Data(
            playlistOf(
                intent = Playlist.Intent.Playlists,
                bursts = (listenedBurstIds + listOf(notListenedBurstId)).map { burstOf(id = it) },
            )
        )
        val listenedBursts = listenedBurstIds.map { listenedBurstOf(id = it) }
        val userResource = Resource.Data(userOf())
        val listenedBurstStorage = listenedBurstsStorageOf(
            getAll = listenedBursts,
        )

        // WHEN
        val resource = interactor(
            getPlaylistByPlaylistInfo = getPlaylistReturn,
            userRepositoryReturns = userResource,
            listenedBurstStorage = listenedBurstStorage,
            userStorage = userStorageOf(filterListenedBursts = false)
        )(playlistInfoOf())

        // THEN
        assertTrue(resource is Result.Data)
        assertEquals(getPlaylistReturn.result.bursts.size, resource.value.bursts.size)
    }

    @Test
    fun testIfTwoFirstBurstsAreReturnedEvenThoughAllBurstsHasBeenAlreadyListenedTo() = runTest {
        // GIVEN
        val listenedBurstIds = listOf("id1", "id2", "id3")
        val getPlaylistReturn = Resource.Data(
            playlistOf(
                intent = Playlist.Intent.Playlists,
                bursts = (listenedBurstIds).map { burstOf(id = it) },
            )
        )
        val listenedBursts = listenedBurstIds.map { listenedBurstOf(id = it) }
        val userResource = Resource.Data(userOf())
        val listenedBurstStorage = listenedBurstsStorageOf(getAll = listenedBursts)

        // WHEN
        val resource = interactor(
            getPlaylistByPlaylistInfo = getPlaylistReturn,
            userRepositoryReturns = userResource,
            listenedBurstStorage = listenedBurstStorage,
            userStorage = userStorageOf(filterListenedBursts = true)
        )(playlistInfoOf())

        // THEN
        assertTrue(resource is Result.Data)
        assertEquals(expected = 2, resource.value.bursts.size)
        assertEquals(getPlaylistReturn.result.bursts[0], resource.value.bursts[0])
        assertEquals(getPlaylistReturn.result.bursts[1], resource.value.bursts[1])
    }

    @Test
    fun `test if playlist is getting shuffled when shuffle is true in Request Options`() = runTest {
        // GIVEN
        val options = requestOptionsOf(shuffle = true)
        val request = channelRequestOf(options = options)
        val burstIds = listOf("id1", "id2", "id3", "id4", "id5", "id6")
        val getPlaylistReturn = Resource.Data(
            playlistOf(
                intent = Playlist.Intent.Playlists,
                bursts = (burstIds).map { burstOf(id = it) },
            )
        )

        // WHEN
        val resource = interactor(
            channelReturns = getPlaylistReturn,
            userRepositoryReturns = Resource.Data(userOf())
        )(request)

        // THEN
        require(resource is Result.Data)
        assertTrue(resource.value.bursts.map { it.id } != burstIds)
    }

    @Test
    fun `test if Burst is not getting filtered out when we pass its Burst id as a firstBurstId in PlaylistRequest's Options`() = runTest {
        // GIVEN
        val firstBurstId = "id7"
        val options = requestOptionsOf(firstBurstId = firstBurstId)
        val request = channelRequestOf(options = options)
        val listenedBurstIds = listOf("id1", "id2", "id3", "id4", "id5", "id6") + listOf(firstBurstId)
        val getPlaylistReturn = Resource.Data(
            playlistOf(
                intent = Playlist.Intent.Playlists,
                bursts = (listenedBurstIds).map { burstOf(id = it) },
            )
        )

        // WHEN
        val resource = interactor(
            channelReturns = getPlaylistReturn,
            userRepositoryReturns = Resource.Data(userOf())
        )(request)

        // THEN
        require(resource is Result.Data)
        assertEquals(firstBurstId, resource.value.bursts.map { it.id }.first())
    }

    @Test
    fun `test if Burst is not getting filtered out when we pass its Burst id as a firstBurstId in PlaylistRequest's Options and shuffle is true`() = runTest {
        // GIVEN
        val firstBurstId = "id7"
        val options = requestOptionsOf(firstBurstId = firstBurstId, shuffle = true)
        val request = channelRequestOf(options = options)
        val listenedBurstIds = listOf("id1", "id2", "id3", "id4", "id5", "id6") + listOf(firstBurstId)
        val getPlaylistReturn = Resource.Data(
            playlistOf(
                intent = Playlist.Intent.Playlists,
                bursts = (listenedBurstIds).map { burstOf(id = it) },
            )
        )

        // WHEN
        val resource = interactor(
            channelReturns = getPlaylistReturn,
            userRepositoryReturns = Resource.Data(userOf())
        )(request)

        // THEN
        require(resource is Result.Data)
        assertEquals(firstBurstId, resource.value.bursts.map { it.id }.first())
    }
}
