package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.*
import com.audioburst.library.models.AnalysisInput
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.Url
import com.audioburst.library.models.InternalPlaybackState
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertEquals

class AdListenedStrategyTest {

    private val strategy = AdListenedStrategy()

    private fun testWithPositions(
        reportingDataPosition: Double,
        currentStatePosition: Double,
        isValid: Boolean
    ) {
        // GIVEN
        val adUrl = "adUrl"
        val adAudioUrl = "adAudioUrl"
        val reportingData = reportingDataOf(
            position = reportingDataPosition,
        )
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    adUrl = adUrl
                )
            )
        )
        val currentState = playbackStateOf(
            url = adAudioUrl,
            position = currentStatePosition,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = emptyArray<InternalPlaybackState>(),
        )

        // WHEN
        val playbackEvent = strategy.check(
            AnalysisInput(
                playlist = playlist,
                currentState = currentState,
                previousStates = previousStates,
                advertisements = listOf(
                    downloadedAdvertisementOf(
                        downloadUrl = Url(adUrl),
                        advertisement = advertisementOf(
                            audioURL = adAudioUrl,
                            reportingData = listOf(
                                reportingData
                            )
                        )
                    )
                ),
            )
        )

        // THEN
        if (isValid) {
            require(playbackEvent is PlaybackEvent.AdListened)
            require(playbackEvent.advertisement.audioURL == adAudioUrl)
            require(playbackEvent.reportingData == reportingData)
        } else {
            assertEquals(null, playbackEvent)
        }
    }

    @Test
    fun testWithDifferentPositions() {
        testWithPositions(
            reportingDataPosition = 10.0,
            currentStatePosition = 11.0,
            isValid = true
        )
        testWithPositions(
            reportingDataPosition = 10.0,
            currentStatePosition = 12.0,
            isValid = true
        )
        testWithPositions(
            reportingDataPosition = 10.0,
            currentStatePosition = 13.0,
            isValid = false
        )
        testWithPositions(
            reportingDataPosition = 10.0,
            currentStatePosition = 9.0,
            isValid = true
        )
        testWithPositions(
            reportingDataPosition = 10.0,
            currentStatePosition = 8.0,
            isValid = true
        )
        testWithPositions(
            reportingDataPosition = 10.0,
            currentStatePosition = 7.0,
            isValid = false
        )
    }

    @Test
    fun testThatEvenThoughBurstContainsAdUrlThatIsAlsoIncludedInAdvertisementButItIsNotPlayingRightNowThenNullIsReturned() {
        // GIVEN
        val adUrl = "adUrl"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    adUrl = adUrl
                )
            )
        )
        val currentState = playbackStateOf(
            url = adUrl,
        )
        val previousStates = fixedQueueOf(
            limit = 10,
            items = emptyArray<InternalPlaybackState>(),
        )

        // WHEN
        val playbackEvent = strategy.check(
            AnalysisInput(
                playlist = playlist,
                currentState = currentState,
                previousStates = previousStates,
                advertisements = listOf(
                    downloadedAdvertisementOf(
                        downloadUrl = Url(adUrl),
                        advertisement = advertisementOf()
                    )
                ),
            )
        )

        // THEN
        assertEquals(null, playbackEvent)
    }
}
