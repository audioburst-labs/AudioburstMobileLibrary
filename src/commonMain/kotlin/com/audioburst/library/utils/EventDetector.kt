package com.audioburst.library.utils

import co.touchlab.stately.concurrency.AtomicReference
import com.audioburst.library.interactors.CurrentAdsProvider
import com.audioburst.library.interactors.CurrentPlaylist
import com.audioburst.library.interactors.PlaybackEventHandler
import com.audioburst.library.models.*
import com.audioburst.library.utils.strategies.ListenedStrategy
import com.audioburst.library.utils.strategies.PlaybackEventStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal interface EventDetector {

    fun start()

    fun stop()

    fun setPlaybackStateListener(listener: PlaybackStateListener)

    fun removePlaybackStateListener(listener: PlaybackStateListener)
}

internal class StrategyBasedEventDetector(
    private val checkInterval: Duration,
    private val currentPlaylist: CurrentPlaylist,
    private val currentAds: CurrentAdsProvider,
    private val playbackEventHandler: PlaybackEventHandler,
    private val strategies: List<PlaybackEventStrategy<*>>,
    private val listenedStrategy: ListenedStrategy,
    private val timestampProvider: TimestampProvider,
    private val appDispatchers: AppDispatchers,
) : EventDetector {

    private val scope = CoroutineScope(appDispatchers.main + SupervisorJob())
    private val previousStates: Queue<InternalPlaybackState> = FixedSizeQueue(NUMBER_OF_CACHED_STATES)
    private val listenerCallTimer: PeriodicTimer = PeriodicTimer()
    private var playbackStateListener: AtomicReference<PlaybackStateListener?> = AtomicReference(null)

    override fun start() {
        startTimers()
        val eventPayload = currentEventPayload(isPlaying = true) ?: return
        handle(PlaybackEvent.Play(eventPayload))
        requestNewState()
    }

    override fun stop() {
        stopTimers()
        val eventPayload = currentEventPayload(isPlaying = false) ?: return
        handle(PlaybackEvent.Pause(eventPayload))
        requestNewState()
    }

    private fun requestNewState() {
        Logger.i("Requesting new playback state")
        playbackStateListener.get()?.getPlaybackState()?.let(::setCurrentState)
    }

    private fun setCurrentState(playbackState: PlaybackState) {
        val input = input(playbackState) ?: return
        Logger.i("PlaybackState: [${input.currentState.url}, ${input.currentState.position.milliseconds}]")
        scope.launch {
            withContext(appDispatchers.background) {
                (listenedStrategy.check(input) + strategies.mapNotNull { it.check(input) })
                    .forEach(this@StrategyBasedEventDetector::handle)
                previousStates.add(input.currentState)
            }
        }
    }

    private fun input(playbackState: PlaybackState): AnalysisInput? {
        val playlist = currentPlaylist() ?: return null
        val state = InternalPlaybackState(
            url = playbackState.url,
            position = playbackState.positionMillis.toDouble().toDuration(DurationUnit.Milliseconds),
            occurrenceTime = timestampProvider.currentTimeMillis()
        )
        return AnalysisInput(
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
            interval = checkInterval
        ).onEach { result ->
            if (result is PeriodicTimer.Result.OnTick) {
                requestNewState()
            }
        }.launchIn(scope)
    }

    private fun stopTimers() {
        listenerCallTimer.pause()
    }

    private fun handle(playbackEvent: PlaybackEvent) {
        scope.launch {
            playbackEventHandler.handle(playbackEvent)
        }
    }

    companion object {
        private const val NUMBER_OF_CACHED_STATES = 10
    }
}
