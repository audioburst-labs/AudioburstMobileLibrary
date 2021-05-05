package com.audioburst.library.utils

import co.touchlab.stately.collections.IsoMutableList
import com.audioburst.library.models.*
import com.audioburst.library.models.eventPayload

internal fun interface PlaybackPeriodsCreator {
    fun check(input: AnalysisInput): List<Result>

    data class Period(val range: LongRange) {
        val duration: Long = range.last - range.first
    }

    data class Result(
        val periods: List<Period>,
        val eventPayload: EventPayload,
    ) {
        val duration: Long = periods.sumOf { it.duration }
    }
}

internal class InputBasedPlaybackPeriodsCreator : PlaybackPeriodsCreator {

    private val periods: IsoMutableList<PlaybackPeriodsCreator.Period> = IsoMutableList()

    override fun check(input: AnalysisInput): List<PlaybackPeriodsCreator.Result> =
        mutableListOf<PlaybackPeriodsCreator.Result?>().apply {
            val currentState = input.currentState
            val previousState = input.lastState()

            if (previousState == null) {
                if (currentState.position.seconds > 0) {
                    periods += PlaybackPeriodsCreator.Period(0L..currentState.positionMilliseconds)
                    add(input.toResult(currentState))
                }
                return@apply
            }

            if (isTheSameMedia(currentState, previousState)) {
                if (isValid(currentState, previousState)) {
                    periods += PlaybackPeriodsCreator.Period(previousState.positionMilliseconds..currentState.positionMilliseconds)
                }
                add(input.toResult(currentState))
            } else {
                periods += PlaybackPeriodsCreator.Period(previousState.positionMilliseconds..previousState.positionMilliseconds + timeLeftInPrevious(
                    currentState,
                    previousState))
                add(input.toResult(previousState))
                periods.clear()
                periods += PlaybackPeriodsCreator.Period(0L..currentState.positionMilliseconds)
                add(input.toResult(currentState))
            }
        }.filterNotNull()

    private fun timeLeftInPrevious(newState: InternalPlaybackState, previousState: InternalPlaybackState): Long =
        (newState.occurrenceTime - previousState.occurrenceTime - newState.position).milliseconds.toLong()

    private fun isValid(newState: InternalPlaybackState, previousState: InternalPlaybackState): Boolean =
        !isSeekBack(newState, previousState) && !isSeekForward(newState, previousState)

    private fun isTheSameMedia(newState: InternalPlaybackState, previousState: InternalPlaybackState): Boolean =
        newState.url == previousState.url

    private fun isSeekBack(newState: InternalPlaybackState, previousState: InternalPlaybackState): Boolean =
        newState.positionMilliseconds < previousState.positionMilliseconds

    private fun isSeekForward(newState: InternalPlaybackState, previousState: InternalPlaybackState): Boolean =
        newState.positionMilliseconds > (previousState.position + newState.occurrenceTime - previousState.occurrenceTime + TIME_DIFFERENCE_THRESHOLD_MS).milliseconds

    private fun AnalysisInput.toResult(playbackState: InternalPlaybackState): PlaybackPeriodsCreator.Result? =
        eventPayload(playbackState)?.let {
            PlaybackPeriodsCreator.Result(
                periods = periods.toList(),
                eventPayload = it,
            )
        }

    companion object {
        private val TIME_DIFFERENCE_THRESHOLD_MS = 50.0.toDuration(DurationUnit.Milliseconds)
    }
}

private val InternalPlaybackState.positionMilliseconds: Long get() = position.milliseconds.toLong()