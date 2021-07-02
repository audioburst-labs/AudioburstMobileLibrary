package com.audioburst.library.utils.strategies

import com.audioburst.library.models.*
import kotlinx.coroutines.flow.*

internal class PlayPauseStrategy(private val minimumWaitTime: Duration = 1.0.toDuration(DurationUnit.Seconds)) {

    private val internalEvent = MutableStateFlow<Event?>(null)
    val event = internalEvent
        .filterNotNull()
        .debounce { it.debounceTimeout.milliseconds.toLong() }
        .distinctUntilChanged()
        .mapNotNull { it.toPlaybackEvent() }

    fun play(eventPayload: EventPayload) {
        val previous = internalEvent.value
        internalEvent.value = if (previous == null || isPlayAfterAWhile(playEventPayload = eventPayload, previous = previous)) {
            Event.PlayImmediately(eventPayload)
        } else {
            Event.Play(eventPayload, minimumWaitTime)
        }
    }

    fun pause(eventPayload: EventPayload) {
        if (internalEvent.value != null) {
            internalEvent.value = Event.Pause(eventPayload, minimumWaitTime)
        }
    }

    private fun isPlayAfterAWhile(playEventPayload: EventPayload, previous: Event): Boolean =
        previous is Event.Pause && playEventPayload.occurrenceTime - previous.payload.occurrenceTime > minimumWaitTime

    private fun Event.toPlaybackEvent(): PlaybackEvent =
        when (this) {
            is Event.Pause -> PlaybackEvent.Pause(payload)
            is Event.Play -> PlaybackEvent.Play(payload)
        }

    private sealed class Event {
        abstract val payload: EventPayload
        abstract val debounceTimeout: Duration

        class Play(override val payload: EventPayload, override val debounceTimeout: Duration) : Event()
        class Pause(override val payload: EventPayload, override val debounceTimeout: Duration) : Event()

        companion object {
            @Suppress("FunctionName")
            fun PlayImmediately(payload: EventPayload): Event =
                Play(payload = payload, debounceTimeout = Duration.ZERO)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            return true
        }

        override fun hashCode(): Int {
            return payload.hashCode()
        }
    }
}