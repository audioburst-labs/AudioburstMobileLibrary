package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.advertisementOf
import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.downloadedAdvertisementOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.*
import com.audioburst.library.utils.*
import kotlin.test.Test
import kotlin.test.assertTrue

class ListenedStrategyTest {

    @Test
    fun testIfTwoSecPlayingEventIsGettingIncluded() {
        // GIVEN
        val url = "url"
        val input = inputOf(
            currentState = playbackStateOf(
                url = url,
                position = 3.0,
            ),
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        audioUrl = url,
                        duration = 12.0.toDuration(DurationUnit.Seconds)
                    )
                )
            )
        )

        // WHEN
        val events = listenedStrategyOf(creator = InputBasedPlaybackPeriodsCreator()).check(input)

        // THEN
        assertTrue(events.filterIsInstance<PlaybackEvent.TwoSecPlaying>().isNotEmpty())
    }

    @Test
    fun testIfPlayingEventIsGettingIncluded() {
        // GIVEN
        val url = "url"
        val input = inputOf(
            currentState = playbackStateOf(
                url = url,
                position = 10.0,
            ),
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        audioUrl = url,
                        duration = 12.0.toDuration(DurationUnit.Seconds)
                    )
                )
            )
        )

        // WHEN
        val events = listenedStrategyOf(creator = InputBasedPlaybackPeriodsCreator()).check(input)

        // THEN
        assertTrue(events.filterIsInstance<PlaybackEvent.Playing>().isNotEmpty())
    }

    @Test
    fun testIfTwoSecADPlayingEventIsGettingIncluded() {
        // GIVEN
        val url = "url"
        val input = inputOf(
            currentState = playbackStateOf(
                url = url,
                position = 3.0,
            ),
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        adUrl = url,
                    )
                )
            ),
            advertisements = listOf(
                downloadedAdvertisementOf(
                    advertisement = advertisementOf(
                        burstUrl = url,
                        duration = 12.0.toDuration(DurationUnit.Seconds)
                    ),
                    downloadUrl = Url(url)
                ),
            )
        )

        // WHEN
        val events = listenedStrategyOf(creator = InputBasedPlaybackPeriodsCreator()).check(input)

        // THEN
        assertTrue(events.filterIsInstance<PlaybackEvent.TwoSecADPlaying>().isNotEmpty())
    }

    @Test
    fun testIfMultipleEventsAreGettingDetected() {
        // GIVEN
        val url = "https://storageaudiobursts.azureedge.net/audio/J8OoDzXD9Pwn.mp3"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = url,
                    duration = 48.0.toDuration(DurationUnit.Seconds)
                )
            )
        )
        val previousStates: Queue<InternalPlaybackState> = FixedSizeQueue(10)
        val listenedStrategy = listenedStrategyOf(
            creator = InputBasedPlaybackPeriodsCreator(),
            factory = listenedMediaStrategyFactoryOf(
                2.0.toDuration(DurationUnit.Seconds)
            ),
        )
        val now = PlatformTimestampProvider.timeSince1970()

        // WHEN
        val events = listOf(
            1.240, 3.243, 5.245, 7.247, 9.249, 11.252, 13.254, 15.256, 17.258, 19.260, 21.262, 23.264, 25.266, 27.268,
            29.270, 31.272, 33.274, 35.276, 37.278, 39.280, 41.282, 43.285, 45.287,
        ).flatMap { position ->
            val input = inputOf(
                currentState = playbackStateOf(
                    url = url,
                    position = position,
                    occurrenceTime = (now + position.toDuration(DurationUnit.Seconds)).milliseconds.toLong()
                ),
                playlist = playlist,
                previousStates = previousStates,
            )
            listenedStrategy.check(input).apply {
                previousStates.add(input.currentState)
            }
        }

        // THEN
        assertTrue(events.filterIsInstance<PlaybackEvent.TwoSecPlaying>().isNotEmpty())
        assertTrue(events.filterIsInstance<PlaybackEvent.BurstListened>().isNotEmpty())
        assertTrue(events.filterIsInstance<PlaybackEvent.Playing>().size == 4)
    }
}
