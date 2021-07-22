package com.audioburst.library.interactors

import com.audioburst.library.data.repository.mappers.libraryConfigurationOf
import com.audioburst.library.data.repository.mappers.playerActionOf
import com.audioburst.library.data.repository.mappers.userStorageOf
import com.audioburst.library.models.*
import com.audioburst.library.runTest
import com.audioburst.library.utils.LibraryConfiguration
import com.audioburst.library.utils.playbackStateOf
import com.audioburst.library.utils.strategies.inputOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UiEventHandlerTest {

    private suspend fun assert(
        userId: String? = null,
        libraryConfiguration: LibraryConfiguration = libraryConfigurationOf(),
        uiEvent: UiEvent = UiEvent.PlaylistClick,
        burstId: String = "",
        isPlaying: Boolean = false,
        analysisInput: AnalysisInput = inputOf(),
        asserter: (List<Event>) -> Unit,
    ) {
        val playbackEventHandler = MemorablePlaybackEventHandler()
        UiEventHandler(
            userStorage = userStorageOf(userId = userId),
            libraryConfiguration = libraryConfiguration,
            playbackEventHandler = playbackEventHandler,
        ).handle(
            uiEvent = uiEvent,
            burstId = burstId,
            isPlaying = isPlaying,
            analysisInput = analysisInput,
        )
        asserter(playbackEventHandler.sentEvents.toList())
    }

    @Test
    fun `test if event is not being sent when userStorage's userId returns null`() = runTest {
        // GIVEN
        val userId = null

        // WHEN
        assert(userId = userId) { sentEvents ->
            // THEN
            assertTrue(sentEvents.isEmpty())
        }
    }

    @Test
    fun `test if event is not being sent when AnalysisInput's playlist is empty userId returns null`() = runTest {
        // GIVEN
        val userId = ""
        val playlist = playlistOf(bursts = emptyList())
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
        ) { sentEvents ->
            // THEN
            assertTrue(sentEvents.isEmpty())
        }
    }

    @Test
    fun `test if event is not being sent when AnalysisInput's playlist is not empty, but it doesn't contain Burst with provided burstId`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = "")))
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            assertTrue(sentEvents.isEmpty())
        }
    }

    @Test
    fun `test if event is being sent when AnalysisInput's playlist is not empty and it contains Burst with provided burstId`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            assertTrue(sentEvents.isNotEmpty())
        }
    }

    @Test
    fun `test if sent event has correct userId`() = runTest {
        // GIVEN
        val userId = "userId"
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(userId, uiEvent.playerEvent.userId)
        }
    }

    @Test
    fun `test if sent event has correct burstId`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(burstId, uiEvent.playerEvent.burstId)
        }
    }

    @Test
    fun `test if sent event has correct libraryConfiguration`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val libraryConfiguration = libraryConfigurationOf(
            sessionId = "sessionId",
            libraryKey = "",
            libraryVersion = "",
            subscriptionKey = "",
        )
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
            libraryConfiguration = libraryConfiguration,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(libraryConfiguration, uiEvent.playerEvent.libraryConfiguration)
        }
    }

    @Test
    fun `test if sent event has correct time`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val occurrenceTime = 100L
        val analysisInput = inputOf(
            playlist = playlist,
            currentState = playbackStateOf(occurrenceTime = occurrenceTime),
        )

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(occurrenceTime, uiEvent.playerEvent.time)
        }
    }

    @Test
    fun `test if sent event has correct playlistName`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlistName = "playlistName"
        val playlist = playlistOf(
            bursts = listOf(burstOf(id = burstId)),
            name = playlistName,
        )
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(playlistName, uiEvent.playerEvent.playlistName)
        }
    }

    @Test
    fun `test if sent event has correct burstLength`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val burstLength = 100.0
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    duration = burstLength.toDuration(DurationUnit.Milliseconds)
                )
            ),
        )
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(burstLength / 1000, uiEvent.playerEvent.burstLength)
        }
    }

    @Test
    fun `test if sent event has correct playerInstanceId`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playerInstanceId = "playerInstanceId"
        val playlist = playlistOf(
            bursts = listOf(burstOf(id = burstId)),
            playerSessionId = PlayerSessionId(playerInstanceId),
        )
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(playerInstanceId, uiEvent.playerEvent.playerInstanceId)
        }
    }

    @Test
    fun `test if sent event has Playing playerStatus when isPlaying is true`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
            isPlaying = true,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(PlayerEvent.Status.Playing, uiEvent.playerEvent.playerStatus)
        }
    }

    @Test
    fun `test if sent event has Stopped playerStatus when isPlaying is false`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
            isPlaying = false,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(PlayerEvent.Status.Stopped, uiEvent.playerEvent.playerStatus)
        }
    }

    @Test
    fun `test if sent event has correct playlistQueryId`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlistId = 1L
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    playlistId = playlistId,
                )
            )
        )
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(playlistId, uiEvent.playerEvent.playlistQueryId)
        }
    }

    @Test
    fun `test if sent event has correct playlistId`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlistId = "playlistId"
        val playlist = playlistOf(
            bursts = listOf(burstOf(id = burstId)),
            id = playlistId,
        )
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(playlistId, uiEvent.playerEvent.playlistId)
        }
    }

    @Test
    fun `test if sent event has correct totalPlayTime`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val burstLength = 100.0
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    duration = burstLength.toDuration(DurationUnit.Milliseconds)
                )
            ),
        )
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(burstLength / 1000, uiEvent.playerEvent.totalPlayTime)
        }
    }

    @Test
    fun `test if sent event has correct pageViewId`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val pageViewId = "pageViewId"
        val playlist = playlistOf(
            bursts = listOf(burstOf(id = burstId)),
            playerSessionId = PlayerSessionId(pageViewId),
        )
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(pageViewId, uiEvent.playerEvent.pageViewId)
        }
    }

    @Test
    fun `test if sent event has correct advertisementEvent`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(null, uiEvent.playerEvent.advertisementEvent)
        }
    }

    @Test
    fun `test if sent event has correct action`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playerAction = playerActionOf(
            type = PlayerAction.Type.Channel,
            value = "10"
        )
        val playlist = playlistOf(
            bursts = listOf(burstOf(id = burstId)),
            playerAction = playerAction,
        )
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(playerAction, uiEvent.playerEvent.action)
        }
    }

    private suspend fun testCtaData(ctaData: CtaData? = null, asserter: PlayerEvent.() -> Unit) {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    ctaData = ctaData,
                )
            ),
        )
        val analysisInput = inputOf(playlist = playlist)

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
            uiEvent = UiEvent.SkipAdClick,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            asserter(uiEvent.playerEvent)
        }
    }

    @Test
    fun `test if sent event has ctaButtonText equal to null when Burst doesn't have CtaData`() = runTest {
        val ctaData = null
        testCtaData(ctaData = ctaData) {
            assertEquals(null, ctaButtonText)
        }
    }

    @Test
    fun `test if sent event has ctaButtonText equal to correctValue when Burst doesn't have CtaData`() = runTest {
        val buttonText = "buttonText"
        val ctaData = ctaDataOf(buttonText = buttonText)
        testCtaData(ctaData = ctaData) {
            assertEquals(buttonText, ctaButtonText)
        }
    }

    @Test
    fun `test if sent event has ctaUrl equal to null when Burst doesn't have CtaData`() = runTest {
        val ctaData = null
        testCtaData(ctaData = ctaData) {
            assertEquals(null, ctaUrl)
        }
    }

    @Test
    fun `test if sent event has ctaUrl equal to correctValue when Burst doesn't have CtaData`() = runTest {
        val url = "url"
        val ctaData = ctaDataOf(url = url)
        testCtaData(ctaData = ctaData) {
            assertEquals(url, ctaUrl)
        }
    }

    @Test
    fun `test if sent event has positionInBurst equal to null when currentPlaybackState has default values`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val defaultPlaybackState = PlaybackState.DEFAULT
        val analysisInput = inputOf(
            playlist = playlist,
            currentState = playbackStateOf(
                url = defaultPlaybackState.url,
                position = defaultPlaybackState.positionMillis.toDouble(),
            )
        )

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(null, uiEvent.playerEvent.positionInBurst)
        }
    }

    @Test
    fun `test if sent event has correct positionInBurst when currentPlaybackState url is the same as Burst's audioUrl`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val audioUrl = "audioUrl"
        val position = 5.0
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    audioUrl = audioUrl,
                )
            )
        )
        val analysisInput = inputOf(
            playlist = playlist,
            currentState = playbackStateOf(
                url = audioUrl,
                position = position,
            )
        )

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(position, uiEvent.playerEvent.positionInBurst)
        }
    }

    @Test
    fun `test if sent event has correct positionInBurst when currentPlaybackState url is the same as Burst's streamUrl`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val streamUrl = "streamUrl"
        val position = 5.0
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    streamUrl = streamUrl,
                )
            )
        )
        val analysisInput = inputOf(
            playlist = playlist,
            currentState = playbackStateOf(
                url = streamUrl,
                position = position,
            )
        )

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(position, uiEvent.playerEvent.positionInBurst)
        }
    }

    @Test
    fun `test if sent event has correct positionInBurst when currentPlaybackState url is the same as Burst's advertisement url`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val adUrl = "adUrl"
        val advertisementAudioUrl = "advertisementAudioUrl"
        val position = 5.0
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    adUrl = adUrl,
                )
            )
        )
        val analysisInput = inputOf(
            playlist = playlist,
            currentState = playbackStateOf(
                url = advertisementAudioUrl,
                position = position,
            ),
            advertisements = listOf(
                downloadedAdvertisementOf(
                    downloadUrl = Url(adUrl),
                    advertisementOf(
                        burstUrl = advertisementAudioUrl,
                    )
                )
            )
        )

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(position, uiEvent.playerEvent.positionInBurst)
        }
    }

    @Test
    fun `test if sent event has stream equal to null when currentPlaybackState has default values`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val playlist = playlistOf(bursts = listOf(burstOf(id = burstId)))
        val defaultPlaybackState = PlaybackState.DEFAULT
        val analysisInput = inputOf(
            playlist = playlist,
            currentState = playbackStateOf(
                url = defaultPlaybackState.url,
                position = defaultPlaybackState.positionMillis.toDouble(),
            )
        )

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(null, uiEvent.playerEvent.stream)
        }
    }

    @Test
    fun `test if sent event has correct stream when currentPlaybackState url is the same as Burst's audioUrl`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val audioUrl = "audioUrl"
        val position = 5.0
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    audioUrl = audioUrl,
                )
            )
        )
        val analysisInput = inputOf(
            playlist = playlist,
            currentState = playbackStateOf(
                url = audioUrl,
                position = position,
            )
        )

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertTrue(uiEvent.playerEvent.stream != null)
        }
    }

    @Test
    fun `test if sent event has true stream when currentPlaybackState url has m3u8 extension`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val audioUrl = "https://storageaudiobursts.azureedge.net/stream/9W7AYBpXJRLn/outputlist.m3u8"
        val position = 5.0
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    audioUrl = audioUrl,
                )
            )
        )
        val analysisInput = inputOf(
            playlist = playlist,
            currentState = playbackStateOf(
                url = audioUrl,
                position = position,
            )
        )

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(true, uiEvent.playerEvent.stream)
        }
    }

    @Test
    fun `test if sent event has false stream when currentPlaybackState url has mp3 extension`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val audioUrl = "https://sapi.audioburst.com/audio/repo/play/mobile/9W7AYBpXJRLn.mp3"
        val position = 5.0
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    audioUrl = audioUrl,
                )
            )
        )
        val analysisInput = inputOf(
            playlist = playlist,
            currentState = playbackStateOf(
                url = audioUrl,
                position = position,
            )
        )

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertEquals(false, uiEvent.playerEvent.stream)
        }
    }

    @Test
    fun `test if sent event has correct stream when currentPlaybackState url is the same as Burst's streamUrl`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val streamUrl = "streamUrl"
        val position = 5.0
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    streamUrl = streamUrl,
                )
            )
        )
        val analysisInput = inputOf(
            playlist = playlist,
            currentState = playbackStateOf(
                url = streamUrl,
                position = position,
            )
        )

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertTrue(uiEvent.playerEvent.stream != null)
        }
    }

    @Test
    fun `test if sent event has correct stream when currentPlaybackState url is the same as Burst's advertisement url`() = runTest {
        // GIVEN
        val userId = ""
        val burstId = "burstId"
        val adUrl = "adUrl"
        val advertisementAudioUrl = "advertisementAudioUrl"
        val position = 5.0
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    adUrl = adUrl,
                )
            )
        )
        val analysisInput = inputOf(
            playlist = playlist,
            currentState = playbackStateOf(
                url = advertisementAudioUrl,
                position = position,
            ),
            advertisements = listOf(
                downloadedAdvertisementOf(
                    downloadUrl = Url(adUrl),
                    advertisementOf(
                        burstUrl = advertisementAudioUrl,
                    )
                )
            )
        )

        // WHEN
        assert(
            userId = userId,
            analysisInput = analysisInput,
            burstId = burstId,
        ) { sentEvents ->
            // THEN
            val uiEvent = sentEvents.first() as GeneralEvent.UiEvent
            assertTrue(uiEvent.playerEvent.stream != null)
        }
    }
}