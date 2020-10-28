package com.audioburst.library.utils

import com.audioburst.library.interactors.*
import com.audioburst.library.models.*
import com.audioburst.library.utils.strategies.PlaybackEventStrategy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.test.Test
import kotlin.test.assertTrue

class EventDetectorTest {

    @Test
    fun testWhenPlayCalledThenPlaybackStatePlayIsGettingHandled() {
        // GIVEN
        val audioUrl = "audioUrl"
        val handledPlaybackEvents = mutableListOf<PlaybackEvent>()
        val eventDetector = eventDetectorOf(
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        audioUrl = audioUrl
                    )
                )
            ),
            playbackEventHandler = { handledPlaybackEvents.add(it) }
        )
        eventDetector.setPlaybackStateListener {
            PlaybackState(
                url = audioUrl,
                positionMillis = 1000
            )
        }

        // WHEN
        eventDetector.play()

        // THEN
        assertTrue(handledPlaybackEvents.filterIsInstance<PlaybackEvent.Play>().isNotEmpty())
    }

    @Test
    fun testWhenPauseCalledThenPlaybackStatePauseIsGettingHandled() {
        // GIVEN
        val audioUrl = "audioUrl"
        val handledPlaybackEvents = mutableListOf<PlaybackEvent>()
        val eventDetector = eventDetectorOf(
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        audioUrl = audioUrl
                    )
                )
            ),
            playbackEventHandler = { handledPlaybackEvents.add(it) }
        )
        eventDetector.setPlaybackStateListener {
            PlaybackState(
                url = audioUrl,
                positionMillis = 1000
            )
        }

        // WHEN
        eventDetector.pause()

        // THEN
        assertTrue(handledPlaybackEvents.filterIsInstance<PlaybackEvent.Pause>().isNotEmpty())
    }

    @Test
    fun testIfStrategyReturnsPlaybackEventThenThisEventIsGettingHandled() {
        // GIVEN
        val handledPlaybackEvents = mutableListOf<PlaybackEvent>()
        val playbackEvent = PlaybackEvent.Play(eventPayloadOf())
        val audioUrl = "audioUrl"
        val eventDetector = eventDetectorOf(
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        audioUrl = audioUrl
                    )
                )
            ),
            playbackEvent = playbackEvent,
            playbackEventHandler = { handledPlaybackEvents.add(it) }
        )
        eventDetector.setPlaybackStateListener {
            PlaybackState(
                url = audioUrl,
                positionMillis = 1000
            )
        }

        // WHEN
        eventDetector.play()

        // THEN
        assertTrue(handledPlaybackEvents.isNotEmpty())
    }
}

internal fun eventDetectorOf(
    playlist: Playlist? = null,
    ads: List<DownloadedAdvertisement> = emptyList(),
    playbackEventHandler: (PlaybackEvent) -> Unit = {},
    playbackEvent: PlaybackEvent = PlaybackEvent.Skip(eventPayloadOf()),
    timestamp: Long = 0,
): EventDetector =
    EventDetector(
        currentPlaylist = currentPlaylistOf(playlist),
        currentAds = currentAdsOf(ads),
        playbackEventHandler = object : PlaybackEventHandler {
            override suspend fun handle(playbackEvent: PlaybackEvent) {
                playbackEventHandler(playbackEvent)
            }
        },
        strategies = listOf(strategyOf(playbackEvent)),
        timestampProvider = timestampProviderOf(timestamp),
        appDispatchers = appDispatchersOf(),
    )

internal fun currentPlaylistOf(playlist: Playlist? = null): CurrentPlaylist =
    CurrentPlaylist { playlist }

internal fun currentAdsOf(ads: List<DownloadedAdvertisement> = emptyList()): CurrentAdsProvider =
    CurrentAdsProvider { ads }

internal fun strategyOf(playbackEvent: PlaybackEvent = PlaybackEvent.Skip(eventPayloadOf())): PlaybackEventStrategy<*> =
    PlaybackEventStrategy { playbackEvent }

internal fun timestampProviderOf(timestamp: Long = 0): TimestampProvider =
    TimestampProvider { timestamp }

internal fun appDispatchersOf(dispatcher: CoroutineDispatcher = Dispatchers.Unconfined): AppDispatchers =
    AppDispatchers(
        io = dispatcher,
        main = dispatcher,
        computation = dispatcher,
    )
