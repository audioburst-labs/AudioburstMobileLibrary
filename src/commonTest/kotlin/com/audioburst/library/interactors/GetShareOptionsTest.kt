package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.appSettingsRepositoryOf
import com.audioburst.library.data.repository.mappers.userStorageOf
import com.audioburst.library.data.repository.shareTextsOf
import com.audioburst.library.models.*
import com.audioburst.library.runTest
import com.audioburst.library.utils.Strings
import com.audioburst.library.utils.currentPlaylistOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetShareOptionsTest {

    private suspend fun assert(
        burstId: String = "",
        userId: String? = null,
        playlist: Playlist? = null,
        shareTexts: ShareTexts = shareTextsOf(),
        burstShareUrl: Resource<BurstShareUrl> = Resource.Data(burstShareUrlOf()),
        url: Url? = null,
        asserter: (ShareOptions?) -> Unit,
    ) {
        val shareOptions = GetShareOptions(
            userStorage = userStorageOf(userId = userId),
            currentPlaylist = currentPlaylistOf(playlist = playlist),
            appSettingsRepository = appSettingsRepositoryOf(shareTexts = shareTexts),
            userRepository = userRepositoryOf(returns = MockUserRepository.Returns(getBurstShareUrl = burstShareUrl)),
            playlistRepository = playlistRepositoryOf(returns = MockPlaylistRepository.Returns(url = url)),
        )(burstId = burstId)
        asserter(shareOptions)
    }

    @Test
    fun `test that null is returned when current Playlist is null`() = runTest {
        // GIVEN
        val playlist = null

        // WHEN
        assert(playlist = playlist) { shareOptions ->
            // THEN
            assertEquals(null, shareOptions)
        }
    }

    @Test
    fun `test that null is returned when current Playlist is not null, but it doesn't contain a burst with provided burstId`() = runTest {
        // GIVEN
        val burstId = "burstId"
        val playlist = playlistOf(bursts = emptyList())

        // WHEN
        assert(
            playlist = playlist,
            burstId = burstId,
        ) { shareOptions ->
            // THEN
            assertEquals(null, shareOptions)
        }
    }

    @Test
    fun `test that null is returned when getBurstShareUrl returns Error`() = runTest {
        // GIVEN
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val burstShareUrl = resourceErrorOf()

        // WHEN
        assert(
            playlist = playlist,
            burstId = burstId,
            burstShareUrl = burstShareUrl,
        ) { shareOptions ->
            // THEN
            assertEquals(null, shareOptions)
        }
    }

    @Test
    fun `test that ShareOptions's burst is not null when burstShareUrl returns Data`() = runTest {
        // GIVEN
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val burstShareUrl = Resource.Data(burstShareUrlOf())

        // WHEN
        assert(
            playlist = playlist,
            burstId = burstId,
            burstShareUrl = burstShareUrl,
        ) { shareOptions ->
            // THEN
            assertTrue(shareOptions?.burst != null)
        }
    }

    @Test
    fun `test that ShareOptions's playlist is null when UserStorage's userId returns null`() = runTest {
        // GIVEN
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val burstShareUrl = Resource.Data(burstShareUrlOf())
        val userId = null

        // WHEN
        assert(
            userId = userId,
            playlist = playlist,
            burstId = burstId,
            burstShareUrl = burstShareUrl,
        ) { shareOptions ->
            // THEN
            assertEquals(null, shareOptions?.playlist)
        }
    }

    private suspend fun assertTexts(
        burstTitle: String = "",
        playlistName: String = "",
        shortUrl: String = "",
        playlistShareUrl: String = "",
        shareTexts: ShareTexts = shareTextsOf(),
        asserter: (ShareOptions) -> Unit,
    ) = runTest {
        // GIVEN
        val burstId = "burstId"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    title = burstTitle,
                )
            ),
            name = playlistName,
        )
        val burstShareUrl = Resource.Data(burstShareUrlOf(shortUrl = shortUrl))
        val userId = ""

        // WHEN
        assert(
            userId = userId,
            playlist = playlist,
            burstId = burstId,
            burstShareUrl = burstShareUrl,
            shareTexts = shareTexts,
            url = Url(playlistShareUrl),
        ) { shareOptions ->
            // THEN
            require(shareOptions != null)
            asserter(shareOptions)
        }
    }

    @Test
    fun `test that shortUrl is used as a title when shareTexts's burst doesn't contain url placeholder`() = runTest {
        // GIVEN
        val shareTexts = shareTextsOf(burst = "")
        val shortUrl = "shortUrl"

        // WHEN
        assertTexts(
            shareTexts = shareTexts,
            shortUrl = shortUrl,
        ) { shareOptions ->
            // THEN
            assertEquals(shortUrl, shareOptions.burst.title)
        }
    }

    @Test
    fun `test that urlPlaceholder is getting replaced in shareOptions burst's title with shortUrl`() = runTest {
        // GIVEN
        val shareTexts = shareTextsOf(burst = "Burst ${Strings.urlPlaceholder}")
        val shortUrl = "shortUrl"

        // WHEN
        assertTexts(
            shareTexts = shareTexts,
            shortUrl = shortUrl,
        ) { shareOptions ->
            // THEN
            assertTrue(shareOptions.burst.title.contains(shortUrl))
        }
    }

    @Test
    fun `test that namePlaceholder is getting replaced in shareOptions burst's title with burstTitle`() = runTest {
        // GIVEN
        val shareTexts = shareTextsOf(burst = "Burst ${Strings.urlPlaceholder}, ${Strings.namePlaceholder}")
        val shortUrl = "shortUrl"
        val burstTitle = "burstTitle"

        // WHEN
        assertTexts(
            shareTexts = shareTexts,
            shortUrl = shortUrl,
            burstTitle = burstTitle,
        ) { shareOptions ->
            // THEN
            assertTrue(shareOptions.burst.title.contains(burstTitle))
        }
    }

    @Test
    fun `test that ShareOptions burst's message contains burstTitle`() = runTest {
        // GIVEN
        val burstTitle = "burstTitle"

        // WHEN
        assertTexts(burstTitle = burstTitle) { shareOptions ->
            // THEN
            assertTrue(shareOptions.burst.message.contains(burstTitle))
        }
    }

    @Test
    fun `test that ShareOptions burst's url is equal to shortUrl`() = runTest {
        // GIVEN
        val shortUrl = "shortUrl"

        // WHEN
        assertTexts(shortUrl = shortUrl) { shareOptions ->
            // THEN
            assertEquals(shortUrl, shareOptions.burst.url)
        }
    }

    @Test
    fun `test that playlistShareUrl is used as a title when shareTexts's playlist doesn't contain url placeholder`() = runTest {
        // GIVEN
        val shareTexts = shareTextsOf(playlist = "")
        val playlistShareUrl = "playlistShareUrl"

        // WHEN
        assertTexts(
            shareTexts = shareTexts,
            playlistShareUrl = playlistShareUrl,
        ) { shareOptions ->
            // THEN
            assertEquals(playlistShareUrl, shareOptions.playlist?.title)
        }
    }

    @Test
    fun `test that urlPlaceholder is getting replaced in shareOptions playlist's title with shareUrl`() = runTest {
        // GIVEN
        val shareTexts = shareTextsOf(burst = "Playlist ${Strings.urlPlaceholder}")
        val playlistShareUrl = "playlistShareUrl"

        // WHEN
        assertTexts(
            shareTexts = shareTexts,
            playlistShareUrl = playlistShareUrl,
        ) { shareOptions ->
            // THEN
            assertTrue(shareOptions.playlist?.title?.contains(playlistShareUrl) == true)
        }
    }

    @Test
    fun `test that namePlaceholder is getting replaced in shareOptions playlist's title with playlistName`() = runTest {
        // GIVEN
        val shareTexts = shareTextsOf(playlist = "Playlist ${Strings.urlPlaceholder}, ${Strings.namePlaceholder}")
        val shortUrl = "shortUrl"
        val playlistName = "playlistName"

        // WHEN
        assertTexts(
            shareTexts = shareTexts,
            shortUrl = shortUrl,
            playlistName = playlistName,
        ) { shareOptions ->
            // THEN
            assertTrue(shareOptions.playlist?.title?.contains(playlistName) == true)
        }
    }

    @Test
    fun `test that ShareOptions playlist's message contains playlistName`() = runTest {
        // GIVEN
        val playlistName = "playlistName"

        // WHEN
        assertTexts(playlistName = playlistName) { shareOptions ->
            // THEN
            assertTrue(shareOptions.playlist?.message?.contains(playlistName) == true)
        }
    }

    @Test
    fun `test that ShareOptions playlist's url is equal to playlistShareUrl`() = runTest {
        // GIVEN
        val playlistShareUrl = "playlistShareUrl"

        // WHEN
        assertTexts(playlistShareUrl = playlistShareUrl) { shareOptions ->
            // THEN
            assertEquals(playlistShareUrl, shareOptions.playlist?.url)
        }
    }
}