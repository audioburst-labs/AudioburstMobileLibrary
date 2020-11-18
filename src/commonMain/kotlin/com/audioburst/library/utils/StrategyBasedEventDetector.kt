package com.audioburst.library.utils

import co.touchlab.stately.concurrency.AtomicReference
import com.audioburst.library.interactors.CurrentAdsProvider
import com.audioburst.library.interactors.CurrentPlaylist
import com.audioburst.library.interactors.PlaybackEventHandler
import com.audioburst.library.models.*
import com.audioburst.library.utils.strategies.PlaybackEventStrategy
import com.audioburst.library.utils.strategies.currentEventPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal interface EventDetector {

    fun start()

    fun stop()

    fun setPlaybackStateListener(listener: PlaybackStateListener)

    fun removePlaybackStateListener(listener: PlaybackStateListener)
}

internal class StrategyBasedEventDetector(
    private val currentPlaylist: CurrentPlaylist,
    private val currentAds: CurrentAdsProvider,
    private val playbackEventHandler: PlaybackEventHandler,
    private val strategies: List<PlaybackEventStrategy<*>>,
    private val timestampProvider: TimestampProvider,
    appDispatchers: AppDispatchers,
) : EventDetector {

    private val scope = CoroutineScope(appDispatchers.main + SupervisorJob())
    private val previousStates: Queue<InternalPlaybackState> = FixedSizeQueue(NUMBER_OF_CACHED_STATES)
    private val listenerCallTimer: PeriodicTimer = PeriodicTimer()
    private val playingEventTimer: PeriodicTimer = PeriodicTimer()
    private var playbackStateListener: AtomicReference<PlaybackStateListener?> = AtomicReference(null)

    override fun start() {
        startTimers()
        val eventPayload = currentEventPayload(isPlaying = true) ?: return
        handle(PlaybackEvent.Play(eventPayload))
    }

    override fun stop() {
        stopTimers()
        val eventPayload = currentEventPayload(isPlaying = false) ?: return
        handle(PlaybackEvent.Pause(eventPayload))
    }

    private fun setCurrentState(playbackState: PlaybackState) {
        val input = input(playbackState) ?: return
        strategies.mapNotNull {
            it.check(input)
        }.forEach(this@StrategyBasedEventDetector::handle)
        previousStates.add(input.currentState)
    }

    private fun input(playbackState: PlaybackState): PlaybackEventStrategy.Input? {
        val playlist = currentPlaylist() ?: return null
        val state = InternalPlaybackState(
            url = playbackState.url,
            position = playbackState.positionMillis.toDouble().toDuration(DurationUnit.Milliseconds).seconds,
            occurrenceTime = timestampProvider.currentTimeMillis()
        )
        return PlaybackEventStrategy.Input(
            playlist = playlist,
            currentState = state,
            previousStates = previousStates,
            advertisements = currentAds(),
        )
    }

    private fun currentEventPayload(isPlaying: Boolean): EventPayload? =
     playbackStateListener.get()?.getPlaybackState()?.let { playbackState ->
         input(playbackState)?.currentEventPayload(isPlaying = isPlaying)
     }

    override fun setPlaybackStateListener(listener: PlaybackStateListener) {
        playbackStateListener.set(listener)
    }

    override fun removePlaybackStateListener(listener: PlaybackStateListener) {
        if (playbackStateListener.get() == listener) {
            playbackStateListener.set(null)
        }
    }

    private fun startTimers() {
        listenerCallTimer.start(
            interval = LISTENER_CALL_SECONDS_TIMEOUT.toDuration(DurationUnit.Seconds)
        ).onEach { result ->
            if (result is PeriodicTimer.Result.OnTick) {
                playbackStateListener.get()?.getPlaybackState()?.let(this@StrategyBasedEventDetector::setCurrentState)
            }
        }.launchIn(scope)
        playingEventTimer.start(
            interval = PLAYING_EVENT_SECONDS_TIMEOUT.toDuration(DurationUnit.Seconds)
        ).onEach { result ->
            if (result is PeriodicTimer.Result.OnTick) {
                val eventPayload = currentEventPayload(isPlaying = true) ?: return@onEach
                handle(PlaybackEvent.Playing(eventPayload))
            }
        }.launchIn(scope)
    }

    private fun stopTimers() {
        listenerCallTimer.pause()
        playingEventTimer.pause()
    }

    private fun handle(playbackEvent: PlaybackEvent) {
        scope.launch {
            playbackEventHandler.handle(playbackEvent)
        }
    }

    companion object {
        private const val NUMBER_OF_CACHED_STATES = 10
        private const val LISTENER_CALL_SECONDS_TIMEOUT = 2.0
        private const val PLAYING_EVENT_SECONDS_TIMEOUT = 10.0
    }
}