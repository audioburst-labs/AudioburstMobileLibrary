package com.audioburst.library.utils

import com.audioburst.library.interactors.CurrentAdsProvider
import com.audioburst.library.interactors.CurrentPlaylist
import com.audioburst.library.interactors.PlaybackEventHandler
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.PlaybackState
import com.audioburst.library.utils.strategies.PlaybackEventStrategy
import com.audioburst.library.utils.strategies.currentEventPayload

internal class EventDetector(
    private val currentPlaylist: CurrentPlaylist,
    private val currentAds: CurrentAdsProvider,
    private val playbackEventHandler: PlaybackEventHandler,
    private val strategies: List<PlaybackEventStrategy<*>>,
    private val timestampProvider: TimestampProvider,
) {

    private val previousStates: Queue<PlaybackState> = FixedSizeQueue(NUMBER_OF_CACHED_STATES)

    fun play(url: String, position: Double) {
        val eventPayload = input(url, position)?.currentEventPayload() ?: return
        handle(PlaybackEvent.Play(eventPayload))
    }

    fun pause(url: String, position: Double) {
        val eventPayload = input(url, position)?.currentEventPayload() ?: return
        handle(PlaybackEvent.Pause(eventPayload))
    }

    fun setCurrentState(url: String, position: Double) {
        val input = input(url, position) ?: return
        strategies.mapNotNull {
            it.check(input)
        }.forEach(this@EventDetector::handle)
        previousStates.add(input.currentState)
    }

    private fun input(url: String, position: Double): PlaybackEventStrategy.Input? {
        val playlist = currentPlaylist() ?: return null
        val state = PlaybackState(
            url = url,
            position = position,
            occurrenceTime = timestampProvider.currentTimeMillis()
        )
        return PlaybackEventStrategy.Input(
            playlist = playlist,
            currentState = state,
            previousStates = previousStates,
            advertisements = currentAds(),
        )
    }

    private fun handle(playbackEvent: PlaybackEvent) {
        playbackEventHandler.handle(playbackEvent)
    }

    companion object {
        private const val NUMBER_OF_CACHED_STATES = 10
    }
}
