package com.audioburst.library.utils.strategies

import com.audioburst.library.models.EventPayload
import com.audioburst.library.models.PlaybackEvent
import kotlinx.coroutines.flow.*

internal class PlayPauseStrategy {

    private val internalEvent = MutableStateFlow<Event?>(null)
    val event = internalEvent
        .filterNotNull()
        .debounce { it.debounceTimeout }
        .distinctUntilChanged()
        .mapNotNull { it.toPlaybackEvent() }

    fun play(eventPayload: EventPayload) {
        internalEvent.value = if (internalEvent.value == null) {
            Event.PlayImmediately(eventPayload)
        } else {
            Event.Play(eventPayload)
        }
    }

    fun pause(eventPayload: EventPayload) {
        if (internalEvent.value != null) {
            internalEvent.value = Event.Pause(eventPayload)
        }
    }

    private fun Event.toPlaybackEvent(): PlaybackEvent =
        when (this) {
            is Event.Pause -> PlaybackEvent.Pause(payload)
            is Event.Play -> PlaybackEvent.Play(payload)
            is Event.PlayImmediately -> PlaybackEvent.Play(payload)
        }

    private val Event.debounceTimeout: Long
        get() = when (this) {
            is Event.Pause -> MINIMAL_TIME_DIFFERENCE
            is Event.Play -> MINIMAL_TIME_DIFFERENCE
            is Event.PlayImmediately -> 0L
        }

    private sealed class Event {
        abstract val payload: EventPayload
        class Play(override val payload: EventPayload) : Event()
        class PlayImmediately(override val payload: EventPayload) : Event()
        class Pause(override val payload: EventPayload) : Event()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false
            return true
        }

        override fun hashCode(): Int {
            return payload.hashCode()
        }
    }

    companion object {
        private const val MINIMAL_TIME_DIFFERENCE = 1000L
    }
}