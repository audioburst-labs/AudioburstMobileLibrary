package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.advertisementOf
import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.downloadedAdvertisementOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.Url
import com.audioburst.library.models.toDuration
import com.audioburst.library.utils.InputBasedPlaybackPeriodsCreator
import com.audioburst.library.utils.listenedStrategyOf
import com.audioburst.library.utils.playbackStateOf
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
}
