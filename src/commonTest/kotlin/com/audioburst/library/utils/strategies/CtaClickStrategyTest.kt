package com.audioburst.library.utils.strategies

import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.ctaDataOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.utils.fixedQueueOf
import com.audioburst.library.utils.playbackStateOf
import kotlin.test.Test
import kotlin.test.assertEquals

class CtaClickStrategyTest {

    private val strategy = CtaClickStrategy()

    @Test
    fun testIfCtaClickIsReturnedWithCorrectDataWhenBurstIdPassedToTheCheckFunctionBelongsToABurstThatContainsCtaData() {
        // GIVEN
        val buttonText = "buttonText"
        val url = "url"
        val ctaData = ctaDataOf(buttonText, url)
        val burstId = "burstId"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    ctaData = ctaData,
                )
            )
        )

        // WHEN
        val playbackEvent = strategy.check(
            input = inputOf(playlist = playlist),
            burstId = burstId,
        )

        // THEN
        require(playbackEvent is PlaybackEvent.CtaClick)
        require(playbackEvent.buttonText == buttonText)
        require(playbackEvent.url == url)
    }

    @Test
    fun testIfNullIsReturnedWhenBurstIdPassedToTheCheckFunctionDoesNotBelongsToAnyBurstThatContainsCtaData() {
        // GIVEN
        val burstId = "burstId"
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(id = burstId)
            )
        )

        // WHEN
        val playbackEvent = strategy.check(
            input = inputOf(playlist = playlist),
            burstId = burstId,
        )

        // THEN
        assertEquals(null, playbackEvent)
    }

    @Test
    fun testIfNullIsReturnedWhenBurstIdPassedToTheCheckFunctionDoesNotBelongsToAnyBurst() {
        // GIVEN
        val playlist = playlistOf(bursts = listOf(burstOf()))

        // WHEN
        val playbackEvent = strategy.check(
            input = inputOf(playlist = playlist),
            burstId = "",
        )

        // THEN
        assertEquals(null, playbackEvent)
    }

    @Test
    fun testWhenThereWasThePreviousStateAndOccurrenceTimeIsOneSecondBeforePreviousState() {
        // GIVEN
        val burstId = "burstId"
        val currentEventOccurrenceTime = 6000L
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    ctaData = ctaDataOf(),
                )
            )
        )
        val currentState = playbackStateOf(occurrenceTime = currentEventOccurrenceTime)
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(occurrenceTime = currentEventOccurrenceTime - 1000)
            ),
        )

        // WHEN
        val playbackEvent = strategy.check(
            inputOf(
                playlist = playlist,
                currentState = currentState,
                previousStates = previousStates,
                advertisements = emptyList(),
            ),
            burstId = burstId,
        )

        // THEN
        require(playbackEvent is PlaybackEvent.CtaClick)
        require(playbackEvent.eventPayload.isPlaying)
    }

    @Test
    fun testWhenThereWasThePreviousStateAndOccurrenceTimeIsThreeSecondBeforePreviousState() {
        // GIVEN
        val burstId = "burstId"
        val currentEventOccurrenceTime = 6000L
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    ctaData = ctaDataOf(),
                )
            )
        )
        val currentState = playbackStateOf(occurrenceTime = currentEventOccurrenceTime)
        val previousStates = fixedQueueOf(
            limit = 10,
            items = arrayOf(
                playbackStateOf(occurrenceTime = currentEventOccurrenceTime - 3000)
            ),
        )

        // WHEN
        val playbackEvent = strategy.check(
            inputOf(
                playlist = playlist,
                currentState = currentState,
                previousStates = previousStates,
                advertisements = emptyList(),
            ),
            burstId = burstId,
        )

        // THEN
        require(playbackEvent is PlaybackEvent.CtaClick)
        require(!playbackEvent.eventPayload.isPlaying)
    }

    @Test
    fun testWhenThereWereNoPreviousStates() {
        // GIVEN
        val burstId = "burstId"
        val currentEventOccurrenceTime = 6000L
        val playlist = playlistOf(
            bursts = listOf(
                burstOf(
                    id = burstId,
                    ctaData = ctaDataOf(),
                )
            )
        )
        val currentState = playbackStateOf(occurrenceTime = currentEventOccurrenceTime)

        // WHEN
        val playbackEvent = strategy.check(
            inputOf(
                playlist = playlist,
                currentState = currentState,
                advertisements = emptyList(),
            ),
            burstId = burstId,
        )

        // THEN
        require(playbackEvent is PlaybackEvent.CtaClick)
        require(!playbackEvent.eventPayload.isPlaying)
    }
}
