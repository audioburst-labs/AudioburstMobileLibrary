package com.audioburst.library.utils

import com.audioburst.library.interactors.CurrentAdsProvider
import com.audioburst.library.interactors.CurrentPlaylist
import com.audioburst.library.interactors.PlaybackEventHandler
import com.audioburst.library.models.*
import com.audioburst.library.utils.strategies.CtaClickStrategy
import com.audioburst.library.utils.strategies.ListenedStrategy
import com.audioburst.library.utils.strategies.PlayPauseStrategy
import com.audioburst.library.utils.strategies.PlaybackEventStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal interface EventDetector {

    fun start()

    fun stop()

    fun ctaButtonClick(burstId: String)

    fun getPlaylists()

    fun setPlaybackStateListener(listener: PlaybackStateListener)

    fun removePlaybackStateListener(listener: PlaybackStateListener)
}

internal class StrategyBasedEventDetector(
    checkInterval: Duration,
    private val scope: CoroutineScope,
    private val currentPlaylist: CurrentPlaylist,
    private val currentAds: CurrentAdsProvider,
    private val playbackEventHandler: PlaybackEventHandler,
    private val strategies: List<PlaybackEventStrategy<*>>,
    private val listenedStrategy: ListenedStrategy,
    private val timestampProvider: TimestampProvider,
    private val appDispatchers: AppDispatchers,
    private val ctaClickStrategy: CtaClickStrategy,
    private val playPauseStrategy: PlayPauseStrategy,
) : EventDetector {

    private val previousStates: Queue<InternalPlaybackState> = FixedSizeQueue(NUMBER_OF_CACHED_STATES)
    private val listenerCallTimer: PeriodicTimer = PeriodicTimer(interval = checkInterval, scope = scope)
    private var currentPlaybackStateListener by nullableAtomic<PlaybackStateListener>()

    init {
        listenerCallTimer.timer
            .onEach { requestNewState() }
            .launchIn(scope)
        playPauseStrategy.event
            .onEach {
                when (it) {
                    is PlaybackEvent.Play -> startTimers()
                    is PlaybackEvent.Pause -> stopTimers()
                }
                handle(it)
            }
            .launchIn(scope)
    }

    override fun start() {
        val eventPayload = currentEventPayload(isPlaying = true) ?: return
        playPauseStrategy.play(eventPayload)
    }

    override fun stop() {
        val eventPayload = currentEventPayload(isPlaying = false) ?: return
        playPauseStrategy.pause(eventPayload)
    }

    override fun ctaButtonClick(burstId: String) {
        val input = currentPlaybackStateListener?.getPlaybackState()?.let(this@StrategyBasedEventDetector::input) ?: return
        ctaClickStrategy.check(
            input = input,
            burstId = burstId,
        )?.let(this@StrategyBasedEventDetector::handle)
    }

    override fun getPlaylists() {
        handle(GeneralEvent.GetPlaylists())
    }

    private fun requestNewState() {
        Logger.i("Requesting new playback state")
        currentPlaybackStateListener?.getPlaybackState()?.let(::setCurrentState)
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
        currentPlaybackStateListener?.getPlaybackState()?.let { playbackState ->
            input(playbackState)?.currentEventPayload(isPlaying = isPlaying)
        }

    override fun setPlaybackStateListener(listener: PlaybackStateListener) {
        currentPlaybackStateListener = listener
    }

    override fun removePlaybackStateListener(listener: PlaybackStateListener) {
        if (currentPlaybackStateListener == listener) {
            currentPlaybackStateListener = null
        }
    }

    private fun startTimers() {
        listenerCallTimer.start()
    }

    private fun stopTimers() {
        listenerCallTimer.pause()
    }

    private fun handle(event: Event) {
        scope.launch {
            playbackEventHandler.handle(event)
        }
    }

    companion object {
        private const val NUMBER_OF_CACHED_STATES = 10
    }
}
