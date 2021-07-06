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

    @Test
    fun testIfMultipleEventsAreGettingDetected1() {
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

        // WHEN
        val times = listOf(
            1625473267705, 1625473269710, 1625473271714, 1625473273717, 1625473275722, 1625473277731, 1625473279736,
            1625473281741, 1625473283746, 1625473285750, 1625473287755, 1625473289760, 1625473291764, 1625473293769,
            1625473295774, 1625473297778, 1625473299783, 1625473301788, 1625473303793, 1625473305797, 1625473307804,
            1625473309807, 1625473311812, 1625473313816, 1625473315821, 1625473317826,
        )
        val events = listOf(
            2.005, 4.010, 6.016, 8.021, 10.026, 12.032, 14.037, 16.042, 18.048, 20.053, 22.058, 24.064, 26.069, 28.074,
            30.080, 32.085, 34.090, 36.096, 38.101, 40.106, 42.112, 44.117, 46.122, 48.128, 50.133, 52.138,
        ).flatMapIndexed { index, position ->
            val input = inputOf(
                currentState = playbackStateOf(
                    url = url,
                    position = position,
                    occurrenceTime = times[index]
                ),
                playlist = playlist,
                previousStates = previousStates,
            )
            listenedStrategy.check(input).apply {
                previousStates.add(input.currentState)
            }
        }

        // THEN
        println(events.joinToString { it.actionName })
        assertTrue(events.filterIsInstance<PlaybackEvent.TwoSecPlaying>().isNotEmpty())
        assertTrue(events.filterIsInstance<PlaybackEvent.BurstListened>().isNotEmpty())
        assertTrue(events.filterIsInstance<PlaybackEvent.Playing>().size == 4)
    }
}
