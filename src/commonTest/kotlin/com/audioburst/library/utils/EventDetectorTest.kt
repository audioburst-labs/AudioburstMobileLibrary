package com.audioburst.library.utils

import com.audioburst.library.interactors.*
import com.audioburst.library.models.*
import com.audioburst.library.utils.strategies.CtaClickStrategy
import com.audioburst.library.utils.strategies.ListenedMediaStrategy
import com.audioburst.library.utils.strategies.ListenedStrategy
import com.audioburst.library.utils.strategies.PlaybackEventStrategy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.test.Test
import kotlin.test.assertTrue

class EventDetectorTest {

    @Test
    fun testWhenPlayCalledThenPlaybackStatePlayIsGettingHandled() {
        // GIVEN
        val audioUrl = "audioUrl"
        val handledPlaybackEvents = mutableListOf<Event>()
        val eventDetector = eventDetectorOf(
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        audioUrl = audioUrl
                    )
                )
            ),
            playbackEventHandler = { handledPlaybackEvents.add(it) }
        )
        eventDetector.setPlaybackStateListener {
            PlaybackState(
                url = audioUrl,
                positionMillis = 1000
            )
        }

        // WHEN
        eventDetector.start()

        // THEN
        assertTrue(handledPlaybackEvents.filterIsInstance<PlaybackEvent.Play>().isNotEmpty())
    }

    @Test
    fun testWhenPauseCalledThenPlaybackStatePauseIsGettingHandled() {
        // GIVEN
        val audioUrl = "audioUrl"
        val handledPlaybackEvents = mutableListOf<Event>()
        val eventDetector = eventDetectorOf(
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        audioUrl = audioUrl
                    )
                )
            ),
            playbackEventHandler = { handledPlaybackEvents.add(it) }
        )
        eventDetector.setPlaybackStateListener {
            PlaybackState(
                url = audioUrl,
                positionMillis = 1000
            )
        }

        // WHEN
        eventDetector.stop()

        // THEN
        assertTrue(handledPlaybackEvents.filterIsInstance<PlaybackEvent.Pause>().isNotEmpty())
    }

    @Test
    fun testIfStrategyReturnsPlaybackEventThenThisEventIsGettingHandled() {
        // GIVEN
        val handledPlaybackEvents = mutableListOf<Event>()
        val playbackEvent = PlaybackEvent.Play(eventPayloadOf())
        val audioUrl = "audioUrl"
        val eventDetector = eventDetectorOf(
            playlist = playlistOf(
                bursts = listOf(
                    burstOf(
                        audioUrl = audioUrl
                    )
                )
            ),
            playbackEvent = playbackEvent,
            playbackEventHandler = { handledPlaybackEvents.add(it) }
        )
        eventDetector.setPlaybackStateListener {
            PlaybackState(
                url = audioUrl,
                positionMillis = 1000
            )
        }

        // WHEN
        eventDetector.start()

        // THEN
        assertTrue(handledPlaybackEvents.isNotEmpty())
    }
}

internal fun eventDetectorOf(
    playlist: Playlist? = null,
    scope: CoroutineScope = CoroutineScope(appDispatchersOf().main),
    ads: List<DownloadedAdvertisement> = emptyList(),
    playbackEventHandler: (Event) -> Unit = {},
    playbackEvent: PlaybackEvent = PlaybackEvent.Skip(eventPayloadOf()),
    timestamp: Long = 0,
    checkInterval: Duration = 1.0.toDuration(DurationUnit.Seconds),
    listenedStrategy: ListenedStrategy = listenedStrategyOf(),
    ctaClickStrategy: CtaClickStrategy = CtaClickStrategy(),
): StrategyBasedEventDetector =
    StrategyBasedEventDetector(
        currentPlaylist = currentPlaylistOf(playlist),
        currentAds = currentAdsOf(ads),
        playbackEventHandler = object : PlaybackEventHandler {
            override suspend fun handle(event: Event) {
                playbackEventHandler(event)
            }
        },
        strategies = listOf(strategyOf(playbackEvent)),
        timestampProvider = timestampProviderOf(timestamp),
        appDispatchers = appDispatchersOf(),
        checkInterval = checkInterval,
        listenedStrategy = listenedStrategy,
        scope = scope,
        ctaClickStrategy = ctaClickStrategy,
    )

internal fun playbackPeriodsCreatorOf(
    results: List<PlaybackPeriodsCreator.Result> = emptyList(),
): PlaybackPeriodsCreator =
    PlaybackPeriodsCreator { results }

internal fun listenedStrategyOf(
    factory: ListenedMediaStrategy.Factory = listenedMediaStrategyFactoryOf(),
    creator: PlaybackPeriodsCreator = playbackPeriodsCreatorOf(),
): ListenedStrategy =
    ListenedStrategy(
        factory = factory,
        creator = creator,
    )

internal fun listenedMediaStrategyFactoryOf(
    refreshInterval: Duration = 1.0.toDuration(DurationUnit.Seconds)
): ListenedMediaStrategy.Factory =
    ListenedMediaStrategy.Factory(refreshInterval = refreshInterval)

internal fun currentPlaylistOf(playlist: Playlist? = null): CurrentPlaylist =
    CurrentPlaylist { playlist }

internal fun currentAdsOf(ads: List<DownloadedAdvertisement> = emptyList()): CurrentAdsProvider =
    CurrentAdsProvider { ads }

internal fun strategyOf(playbackEvent: PlaybackEvent = PlaybackEvent.Skip(eventPayloadOf())): PlaybackEventStrategy<*> =
    PlaybackEventStrategy { playbackEvent }

internal fun timestampProviderOf(timestamp: Long = 0): TimestampProvider =
    TimestampProvider { timestamp }

internal fun appDispatchersOf(dispatcher: CoroutineDispatcher = Dispatchers.Unconfined): AppDispatchers =
    AppDispatchers(
        main = dispatcher,
        background = dispatcher,
    )
