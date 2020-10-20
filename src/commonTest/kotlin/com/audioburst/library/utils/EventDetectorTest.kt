package com.audioburst.library.utils

import com.audioburst.library.interactors.*
import com.audioburst.library.models.DownloadedAdvertisement
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.Playlist
import com.audioburst.library.utils.strategies.PlaybackEventStrategy
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

        // WHEN
        eventDetector.play(audioUrl, 0.0)

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

        // WHEN
        eventDetector.pause(audioUrl, 0.0)

        // THEN
        assertTrue(handledPlaybackEvents.filterIsInstance<PlaybackEvent.Pause>().isNotEmpty())
    }

    @Test
    fun testIfStrategyReturnsPlaybackEventThenThisEventIsGettingHandled() {
        // GIVEN
        val handledPlaybackEvents = mutableListOf<PlaybackEvent>()
        val playbackEvent = PlaybackEvent.Play(eventPayloadOf())
        val eventDetector = eventDetectorOf(
            playlist = playlistOf(),
            playbackEvent = playbackEvent,
            playbackEventHandler = { handledPlaybackEvents.add(it) }
        )

        // WHEN
        eventDetector.setCurrentState("", 0.0)

        // THEN
        assertTrue(handledPlaybackEvents.contains(playbackEvent))
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
        playbackEventHandler = playbackEventHandler,
        strategies = listOf(strategyOf(playbackEvent)),
        timestampProvider = timestampProviderOf(timestamp),
    )

internal fun currentPlaylistOf(playlist: Playlist? = null): CurrentPlaylist =
    CurrentPlaylist { playlist }

internal fun currentAdsOf(ads: List<DownloadedAdvertisement> = emptyList()): CurrentAdsProvider =
    CurrentAdsProvider { ads }

internal fun strategyOf(playbackEvent: PlaybackEvent = PlaybackEvent.Skip(eventPayloadOf())): PlaybackEventStrategy<*> =
    PlaybackEventStrategy { playbackEvent }

internal fun timestampProviderOf(timestamp: Long = 0): TimestampProvider =
    TimestampProvider { timestamp }
