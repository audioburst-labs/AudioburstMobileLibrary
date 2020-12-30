package com.audioburst.library.utils

import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.playlistOf
import com.audioburst.library.models.*
import com.audioburst.library.utils.strategies.inputOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlaybackPeriodsCreatorTest {

    @Test
    fun testIfPeriodsAreNotCreatedWhenFirstEventHappenedAt0Position() {
        // GIVEN
        val creator = InputBasedPlaybackPeriodsCreator()
        val input = inputOf(
            currentState = playbackStateOf(
                position = 0.0
            )
        )

        // WHEN
        val result = creator.check(input)

        // THEN
        assertTrue(result.isEmpty())
    }

    @Test
    fun testIfPeriodsAreCreatedWhenFirstEventHappenedAtPositionHigherThan0() {
        // GIVEN
        val position = 1.0
        val audioUrl = ""
        val creator = InputBasedPlaybackPeriodsCreator()
        val input = inputOf(
            currentState = playbackStateOf(
                url = audioUrl,
                position = position,
            ),
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        audioUrl = audioUrl
                    )
                )
            )
        )

        // WHEN
        val result = creator.check(input)

        // THEN
        assertEquals(position.secondsToMilliseconds(), result.first().periods.last().range.last)
    }

    @Test
    fun testIfDurationIsCalculatedProperly() {
        // GIVEN
        val position = listOf(1.0, 2.0, 3.0).toSeconds()
        val audioUrl = ""
        val creator = InputBasedPlaybackPeriodsCreator()
        val inputs = position.toInputs(2.5.toDuration(DurationUnit.Seconds)) {
            inputOf(
                currentState = playbackStateOf(
                    url = audioUrl,
                ),
                playlist = playlistOf(
                    bursts = listOf(
                        burstOf(
                            audioUrl = audioUrl
                        )
                    )
                ),
                previousStates = fixedQueueOf(
                    limit = 10,
                    items = arrayOf(
                        playbackStateOf(
                            url = audioUrl
                        )
                    )
                )
            )
        }

        // WHEN
        val lastResult = inputs.map(creator::check).last()

        // THEN
        assertEquals(3000, lastResult.last().duration)
    }

    @Test
    fun testIfDurationIsCalculatedWhenUrlIsChangingToDifferentOne() {
        // GIVEN
        val position = listOf(1.0, 2.0, 1.0).toSeconds()
        val burstUrls = listOf("", "", "url")
        val currentUrls = listOf("", "", "url")
        val previousUrls = listOf("", "", "")
        val creator = InputBasedPlaybackPeriodsCreator()
        val inputs = position.toInputs(1.0.toDuration(DurationUnit.Seconds)) {
            inputOf(
                currentState = playbackStateOf(
                    url = currentUrls[it],
                ),
                playlist = playlistOf(
                    bursts = burstUrls.map { burstUrl ->
                        burstOf(
                            audioUrl = burstUrl
                        )
                    }
                ),
                previousStates = fixedQueueOf(
                    limit = 10,
                    items = arrayOf(
                        playbackStateOf(
                            url = previousUrls[it]
                        )
                    )
                )
            )
        }

        // WHEN
        val lastResult = inputs.map(creator::check).last()

        // THEN
        assertEquals(2000, lastResult.first { it.eventPayload.burst.audioUrl == "" }.duration)
        assertEquals(1000, lastResult.first { it.eventPayload.burst.audioUrl == "url" }.duration)
    }

    @Test
    fun testIfTimeDifferenceBetweenStatesIsGettingAddedWhenUrlIsChangingToDifferentOne() {
        // GIVEN
        val position = listOf(2.1, 4.2, 1.0).toSeconds()
        val burstUrls = listOf("", "", "url")
        val currentUrls = listOf("", "", "url")
        val previousUrls = listOf("", "", "")
        val creator = InputBasedPlaybackPeriodsCreator()
        val inputs = position.toInputs(2.1.toDuration(DurationUnit.Seconds)) {
            inputOf(
                currentState = playbackStateOf(
                    url = currentUrls[it],
                ),
                playlist = playlistOf(
                    bursts = burstUrls.map { burstUrl ->
                        burstOf(
                            audioUrl = burstUrl
                        )
                    }
                ),
                previousStates = fixedQueueOf(
                    limit = 10,
                    items = arrayOf(
                        playbackStateOf(
                            url = previousUrls[it]
                        )
                    )
                )
            )
        }

        // WHEN
        val lastResult = inputs.map(creator::check).last()

        // THEN
        assertEquals(5300, lastResult.first { it.eventPayload.burst.audioUrl == "" }.duration)
        assertEquals(1000, lastResult.first { it.eventPayload.burst.audioUrl == "url" }.duration)
    }

    @Test
    fun testIfDurationsAreNoteGettingAddedWhenUrlIsNotChangingButNextPositionsAreNotRising() {
        // GIVEN
        val position = listOf(1.0, 1.0, 1.0).toSeconds()
        val burstUrls = listOf("", "", "")
        val currentUrls = listOf("", "", "")
        val previousUrls = listOf("", "", "")
        val creator = InputBasedPlaybackPeriodsCreator()
        val inputs = position.toInputs(1.1.toDuration(DurationUnit.Seconds)) {
            inputOf(
                currentState = playbackStateOf(
                    url = currentUrls[it],
                ),
                playlist = playlistOf(
                    bursts = burstUrls.map { burstUrl ->
                        burstOf(
                            audioUrl = burstUrl
                        )
                    }
                ),
                previousStates = fixedQueueOf(
                    limit = 10,
                    items = arrayOf(
                        playbackStateOf(
                            url = previousUrls[it]
                        )
                    )
                )
            )
        }

        // WHEN
        val lastResult = inputs.map(creator::check).last()

        // THEN
        assertEquals(1000, lastResult.first { it.eventPayload.burst.audioUrl == "" }.duration)
    }

    @Test
    fun testIfDurationsAreNoteGettingAddedWhenUrlIsNotChangingButNextPositionsAreFartherInTimeThanTimeBetween() {
        // GIVEN
        val position = listOf(1.0, 2.051, 3.102).toSeconds()
        val burstUrls = listOf("", "", "")
        val currentUrls = listOf("", "", "")
        val previousUrls = listOf("", "", "")
        val creator = InputBasedPlaybackPeriodsCreator()
        val inputs = position.toInputs(1.0.toDuration(DurationUnit.Seconds)) {
            inputOf(
                currentState = playbackStateOf(
                    url = currentUrls[it],
                ),
                playlist = playlistOf(
                    bursts = burstUrls.map { burstUrl ->
                        burstOf(
                            audioUrl = burstUrl
                        )
                    }
                ),
                previousStates = fixedQueueOf(
                    limit = 10,
                    items = arrayOf(
                        playbackStateOf(
                            url = previousUrls[it]
                        )
                    )
                )
            )
        }

        // WHEN
        val lastResult = inputs.map(creator::check).last()

        // THEN
        assertEquals(1000, lastResult.first { it.eventPayload.burst.audioUrl == "" }.duration)
    }

    @Test
    fun testIfDurationsAreNoteGettingAddedWhenUrlIsNotChangingButNextPositionsAreBackInTime() {
        // GIVEN
        val position = listOf(1.0, 0.7, 0.5).toSeconds()
        val burstUrls = listOf("", "", "")
        val currentUrls = listOf("", "", "")
        val previousUrls = listOf("", "", "")
        val creator = InputBasedPlaybackPeriodsCreator()
        val inputs = position.toInputs(1.1.toDuration(DurationUnit.Seconds)) {
            inputOf(
                currentState = playbackStateOf(
                    url = currentUrls[it],
                ),
                playlist = playlistOf(
                    bursts = burstUrls.map { burstUrl ->
                        burstOf(
                            audioUrl = burstUrl
                        )
                    }
                ),
                previousStates = fixedQueueOf(
                    limit = 10,
                    items = arrayOf(
                        playbackStateOf(
                            url = previousUrls[it]
                        )
                    )
                )
            )
        }

        // WHEN
        val lastResult = inputs.map(creator::check).last()

        // THEN
        assertEquals(1000, lastResult.first { it.eventPayload.burst.audioUrl == "" }.duration)
    }
}

private fun Double.secondsToMilliseconds(): Long = (this * 1000).toLong()

private fun List<Double>.toSeconds(): List<Duration> = map { it.toDuration(DurationUnit.Seconds) }

private data class StateTime(
    val current: Duration,
    val previous: Duration?,
)

private fun List<Duration>.stateTimes(): List<StateTime> =
    listOf(StateTime(current = first(), previous = null)) + windowed(2, 1, false) {
        StateTime(current = it.last(), previous = it.first())
    }

private fun List<Duration>.toInputs(timeBetween: Duration, inputCreator: (Int) -> AnalysisInput): List<AnalysisInput> =
    stateTimes()
        .mapIndexed { index, stateTime ->
            inputCreator(index).run {
                copy(
                    currentState = currentState.copy(
                        position = stateTime.current,
                        occurrenceTime = timeBetween.milliseconds.toLong(),
                    ),
                    previousStates = FixedSizeQueue<InternalPlaybackState>(previousStates.size).apply {
                        addAll(
                            stateTime.previous?.let { previousPosition ->
                                previousStates.map {
                                    it.copy(
                                        position = previousPosition,
                                        occurrenceTime = 0
                                    )
                                }
                            } ?: emptyList()
                        )
                    }
                )
            }
        }
