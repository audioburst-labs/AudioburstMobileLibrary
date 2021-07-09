package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.advertisementOf
import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.downloadedAdvertisementOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.*
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SkipStrategyTest {

    private val strategy = SkipStrategy()

    private fun testWithPosition(
        lastStatePosition: Double,
        previousBurstDuration: Double,
        isValid: Boolean,
    ) {
        // GIVEN
        val firstAudioUrl = "firstAudioUrl"
        val secondAudioUrl = "secondAudioUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = firstAudioUrl,
                    duration = previousBurstDuration.toDuration(DurationUnit.Seconds)
                ),
                burstOf(
                    audioUrl = secondAudioUrl,
                )
            )
        )
        val currentState = playbackStateOf(
            url = secondAudioUrl,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = firstAudioUrl,
                    position = lastStatePosition
                )
            )
        )

        // WHEN
        val playbackEvent = strategy.check(
            AnalysisInput(
                playlist = playlist,
                currentState = currentState,
                previousStates = previousStates,
                advertisements = emptyList(),
            )
        )

        // THEN
        if (isValid) {
            assertTrue(playbackEvent != null)
        } else {
            assertTrue(playbackEvent == null)
        }
    }

    @Test
    fun testDifferentPositions() {
        testWithPosition(
            lastStatePosition = 80.0,
            previousBurstDuration = 100.0,
            isValid = true
        )

        testWithPosition(
            lastStatePosition = 89.0,
            previousBurstDuration = 100.0,
            isValid = true
        )

        testWithPosition(
            lastStatePosition = 89.0,
            previousBurstDuration = 0.0,
            isValid = true
        )

        testWithPosition(
            lastStatePosition = 95.0,
            previousBurstDuration = 100.0,
            isValid = false
        )
    }

    @Test
    fun testWhenThereIsNoPreviousState() {
        // GIVEN
        val playlist = playlistOf()
        val currentState = playbackStateOf()
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf<InternalPlaybackState>()
        )

        // WHEN
        val playbackEvent = strategy.check(
            AnalysisInput(
                playlist = playlist,
                currentState = currentState,
                previousStates = previousStates,
                advertisements = emptyList(),
            )
        )

        // THEN
        assertEquals(null, playbackEvent)
    }

    @Test
    fun `test `() {
        // GIVEN
        val burstAudioUrl = "burstAudioUrl"
        val getAdUrl = "getAdUrl"
        val adUrl = "adUrl"
        val burstDuration = 15.0.toDuration(DurationUnit.Seconds)
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = burstAudioUrl,
                    duration = burstDuration,
                    adUrl = getAdUrl,
                ),
            )
        )
        val currentState = playbackStateOf(
            url = burstAudioUrl,
            position = 7.85,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = adUrl,
                    position = 11.350,
                )
            )
        )
        val advertisement = downloadedAdvertisementOf(
            downloadUrl = Url(getAdUrl),
            advertisement = advertisementOf(
                burstUrl = adUrl,
            )
        )

        // WHEN
        val playbackEvent = strategy.check(
            AnalysisInput(
                playlist = playlist,
                currentState = currentState,
                previousStates = previousStates,
                advertisements = listOf(advertisement),
            )
        )

        // THEN
        assertTrue(playbackEvent == null)
    }

    @Test
    fun `test lol`() {
        // GIVEN
        val firstBurstAudioUrl = "https://storageaudiobursts.azureedge.net/audio/lKd3NlOJJZMd.mp3"
        val secondBurstAudioUrl = "https://storageaudiobursts.azureedge.net/audio/ZXXnY8BD3dRL.mp3"
        val getAdUrl = "getAdUrl"
        val adUrl = "http://storageaudiobursts.blob.core.windows.net/ads/source/c97f3966-abbc-42bc-9561-aa93605dede4.mp3"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    audioUrl = firstBurstAudioUrl,
                    duration = 45.01.toDuration(DurationUnit.Seconds),
                ),
                burstOf(
                    audioUrl = secondBurstAudioUrl,
                    duration = 44.64.toDuration(DurationUnit.Seconds),
                    adUrl = getAdUrl,
                ),
            )
        )
        val currentState = playbackStateOf(
            url = adUrl,
            position = 3.324,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(
                    url = firstBurstAudioUrl,
                    position = 11.868,
                )
            )
        )
        val advertisement = downloadedAdvertisementOf(
            downloadUrl = Url(getAdUrl),
            advertisement = advertisementOf(
                burstUrl = adUrl,
            )
        )

        // WHEN
        val playbackEvent = strategy.check(
            AnalysisInput(
                playlist = playlist,
                currentState = currentState,
                previousStates = previousStates,
                advertisements = listOf(advertisement),
            )
        )

        // THEN
        assertTrue(playbackEvent != null)
    }
}
