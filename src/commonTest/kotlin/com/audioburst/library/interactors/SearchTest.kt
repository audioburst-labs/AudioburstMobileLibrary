package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.storage.InMemoryPlaylistStorage
import com.audioburst.library.models.LibraryError
import com.audioburst.library.models.Playlist
import com.audioburst.library.models.Result
import com.audioburst.library.models.User
import com.audioburst.library.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchTest {

    private lateinit var playlistStorage: InMemoryPlaylistStorage
    private fun interactor(
        search: Resource<Playlist> = Resource.Data(playlistOf()),
        getPlaylistByByteArray: Resource<Playlist> = Resource.Data(playlistOf()),
        userResource: Resource<User>,
        postContentLoadEvent: PostContentLoadEvent = postContentLoadEventOf(),
    ): Search =
        Search(
            getUser = getUserOf(userResource),
            userRepository = userRepositoryOf(
                returns = MockUserRepository.Returns(
                    search = search,
                    getPlaylistByByteArray = getPlaylistByByteArray,
                )
            ),
            postContentLoadEvent = postContentLoadEvent,
            playlistStorage = playlistStorage,
        )

    @BeforeTest
    fun setup() {
        playlistStorage = InMemoryPlaylistStorage()
    }

    @Test
    fun testIfResultDataIsReturnedWhenSearchReturnsPlaylistWithAtLeastOneBurst() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf(bursts = listOf(burstOf())))
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            search = getPlaylistReturn,
            userResource = userResource,
        )(query = "")

        // THEN
        assertTrue(resource is Result.Data)
        assertTrue(playlistStorage.currentPlaylist != null)
    }

    @Test
    fun testIfResultDataIsReturnedWhenSearchWithByteArrayReturnsPlaylistWithAtLeastOneBurst() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf(bursts = listOf(burstOf())))
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
    fun testIfNoSearchResultsIsReturnedWhenSearchReturnsPlaylistWithoutBursts() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf())
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            search = getPlaylistReturn,
            userResource = userResource,
        )(query = "")

        // THEN
        require(resource is Result.Error)
        assertEquals(LibraryError.NoSearchResults, resource.error)
        assertTrue(playlistStorage.currentPlaylist == null)
    }

    @Test
    fun testIfNoSearchResultsIsReturnedWhenSearchWithByteArrayReturnsPlaylistWithoutBursts() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf())
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            getPlaylistByByteArray = getPlaylistReturn,
            userResource = userResource,
        )(byteArrayOf())

        // THEN
        require(resource is Result.Error)
        assertEquals(LibraryError.NoSearchResults, resource.error)
        assertTrue(playlistStorage.currentPlaylist == null)
    }

    @Test
    fun testIfGetUserReturnsErrorThenErrorIsReturnedSearchIsCalled() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf())
        val userResource = resourceErrorOf()

        // WHEN
        val resource = interactor(
            getPlaylistByByteArray = getPlaylistReturn,
            userResource = userResource,
        )(query = "")

        // THEN
        assertTrue(resource is Result.Error)
        assertTrue(playlistStorage.currentPlaylist == null)
    }

    @Test
    fun testIfGetUserReturnsErrorThenErrorIsReturnedSearchWithByteArrayIsCalled() = runTest {
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
        assertTrue(playlistStorage.currentPlaylist == null)
    }

    @Test
    fun testIfGetUserReturnsUserAndSearchReturnsErrorThenErrorIsReturned() = runTest {
        // GIVEN
        val getPlaylistReturn = resourceErrorOf()
        val userResource = Resource.Data(userOf())

        // WHEN
        val resource = interactor(
            getPlaylistByByteArray = getPlaylistReturn,
            userResource = userResource,
        )(query = "")

        // THEN
        assertTrue(resource is Result.Error)
        assertTrue(playlistStorage.currentPlaylist == null)
    }

    @Test
    fun testIfGetUserReturnsUserAndSearchWithByteArrayReturnsErrorThenErrorIsReturned() = runTest {
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
        assertTrue(playlistStorage.currentPlaylist == null)
    }

    @Test
    fun testIfContentLoadEventIsGettingSentWhenSearchIsSuccessful() = runTest {
        // GIVEN
        val getPlaylistReturn = Resource.Data(playlistOf(bursts = listOf(burstOf())))
        val userResource = Resource.Data(userOf())
        val playbackEventHandler = MemorablePlaybackEventHandler()

        // WHEN
        interactor(
            search = getPlaylistReturn,
            userResource = userResource,
            postContentLoadEvent = postContentLoadEventOf(
                playbackEventHandler = playbackEventHandler
            )
        )(query = "")

        // THEN
        assertTrue(playbackEventHandler.sentEvents.isNotEmpty())
    }

    @Test
    fun testIfContentLoadEventIsGettingSentWhenSearchWithByteArrayIsSuccessful() = runTest {
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
