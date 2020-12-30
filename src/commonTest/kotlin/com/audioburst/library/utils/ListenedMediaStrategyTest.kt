package com.audioburst.library.utils

import com.audioburst.library.interactors.advertisementOf
import com.audioburst.library.interactors.burstOf
import com.audioburst.library.interactors.downloadedAdvertisementOf
import com.audioburst.library.interactors.eventPayloadOf
import com.audioburst.library.models.*
import com.audioburst.library.utils.strategies.ListenedMediaStrategy
import com.audioburst.library.utils.strategies.ListenedMediaStrategy.Configuration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListenedMediaStrategyTest {

    @Test
    fun testThatForEmptyPeriodResultsItReturnsNull() {
        // GIVEN
        val advertisements = emptyList<DownloadedAdvertisement>()
        val periodResult = periodResult()
        val strategy = listenedMediaStrategyConfigurationOf()

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results == null)
    }

    @Test
    fun testThatWhenStrategyIsOneOffAndBurstAndReturnedPeriodsAreWithinBurstDurationItReturnsValue() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val advertisements = emptyList<DownloadedAdvertisement>()
        val periodResult = periodResult(
            eventPayload = eventPayloadOf(
                burst = burstOf(
                    duration = 5.0.toDuration(DurationUnit.Seconds)
                )
            ),
            periods = listOf(
                periodOf(0L..2000L)
            )
        )
        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.OneOff,
            timeOf = Configuration.TimeOf.Burst,
            minimumListenTime = minimumListenTime,
        )

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results != null)
    }

    @Test
    fun testThatWhenStrategyIsOneOffAndAdvertisementAndReturnedPeriodsAreWithinBurstDurationItReturnsNull() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val advertisements = emptyList<DownloadedAdvertisement>()
        val periodResult = periodResult(
            eventPayload = eventPayloadOf(
                burst = burstOf(
                    duration = 5.0.toDuration(DurationUnit.Seconds)
                )
            ),
            periods = listOf(
                periodOf(0L..2000L)
            )
        )
        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.OneOff,
            timeOf = Configuration.TimeOf.Advertisement,
            minimumListenTime = minimumListenTime,
        )

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results == null)
    }

    @Test
    fun testThatWhenStrategyIsOneOffAndBurstAndReturnedPeriodsAreNotWithinBurstDurationItReturnsNull() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val burstDuration = 5.0.toDuration(DurationUnit.Seconds)
        val advertisements = emptyList<DownloadedAdvertisement>()
        val periodResult = periodResult(
            eventPayload = eventPayloadOf(
                burst = burstOf(
                    duration = burstDuration
                )
            ),
            periods = listOf(
                periodOf(burstDuration.milliseconds.toLong()..burstDuration.milliseconds.toLong() + minimumListenTime.milliseconds.toLong())
            )
        )
        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.OneOff,
            timeOf = Configuration.TimeOf.Advertisement,
            minimumListenTime = minimumListenTime,
        )

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results == null)
    }

    @Test
    fun testThatWhenStrategyIsPeriodicalAndBurstAndReturnedPeriodsAreWithinBurstDurationItReturnsValue() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val advertisements = emptyList<DownloadedAdvertisement>()
        val periodResult = periodResult(
            eventPayload = eventPayloadOf(
                burst = burstOf(
                    duration = 5.0.toDuration(DurationUnit.Seconds)
                )
            ),
            periods = listOf(
                periodOf(0L..2000L)
            )
        )
        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.Periodical,
            timeOf = Configuration.TimeOf.Burst,
            minimumListenTime = minimumListenTime,
        )

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results != null)
    }

    @Test
    fun testThatWhenStrategyIsPeriodicalAndAdvertisementAndReturnedPeriodsAreWithinBurstDurationItReturnsNull() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val advertisements = emptyList<DownloadedAdvertisement>()
        val periodResult = periodResult(
            eventPayload = eventPayloadOf(
                burst = burstOf(
                    duration = 5.0.toDuration(DurationUnit.Seconds)
                )
            ),
            periods = listOf(
                periodOf(0L..2000L)
            )
        )
        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.Periodical,
            timeOf = Configuration.TimeOf.Advertisement,
            minimumListenTime = minimumListenTime,
        )

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results == null)
    }

    @Test
    fun testThatWhenStrategyIsPeriodicalAndBurstAndReturnedPeriodsAreNotWithinBurstDurationItReturnsNull() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val burstDuration = 5.0.toDuration(DurationUnit.Seconds)
        val advertisements = emptyList<DownloadedAdvertisement>()
        val periodResult = periodResult(
            eventPayload = eventPayloadOf(
                burst = burstOf(
                    duration = burstDuration
                )
            ),
            periods = listOf(
                periodOf(burstDuration.milliseconds.toLong()..burstDuration.milliseconds.toLong() + 1000)
            )
        )
        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.Periodical,
            timeOf = Configuration.TimeOf.Advertisement,
            minimumListenTime = minimumListenTime,
        )

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results == null)
    }

    @Test
    fun testThatWhenStrategyIsPeriodicalAndAdvertisementAndReturnedPeriodsAreNotWithinAdvertisementDurationItReturnsNull() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val adDuration = 5.0.toDuration(DurationUnit.Seconds)
        val adUrl = Url("adUrl")
        val advertisements = listOf(
            downloadedAdvertisementOf(
                downloadUrl = adUrl,
                advertisement = advertisementOf(
                    duration = adDuration
                )
            )
        )
        val periodResult = periodResult(
            eventPayload = eventPayloadOf(
                burst = burstOf(
                    adUrl = adUrl.value
                )
            ),
            periods = listOf(
                periodOf(adDuration.milliseconds.toLong()..adDuration.milliseconds.toLong() + 1000)
            )
        )
        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.Periodical,
            timeOf = Configuration.TimeOf.Advertisement,
            minimumListenTime = minimumListenTime,
        )

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results == null)
    }

    @Test
    fun testThatWhenStrategyIsOneOffAndAdvertisementAndReturnedPeriodsAreNotWithinAdvertisementDurationItReturnsNull() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val adDuration = 5.0.toDuration(DurationUnit.Seconds)
        val adUrl = Url("adUrl")
        val advertisements = listOf(
            downloadedAdvertisementOf(
                downloadUrl = adUrl,
                advertisement = advertisementOf(
                    duration = adDuration
                )
            )
        )
        val periodResult = periodResult(
            eventPayload = eventPayloadOf(
                burst = burstOf(
                    adUrl = adUrl.value
                )
            ),
            periods = listOf(
                periodOf(adDuration.milliseconds.toLong()..adDuration.milliseconds.toLong() + 1000)
            )
        )
        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.OneOff,
            timeOf = Configuration.TimeOf.Advertisement,
            minimumListenTime = minimumListenTime,
        )

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results == null)
    }

    @Test
    fun testThatWhenStrategyIsPeriodicalAndAdvertisementAndReturnedPeriodsAreWithinAdvertisementDurationItReturnsValue() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val adDuration = 5.0.toDuration(DurationUnit.Seconds)
        val adUrl = Url("adUrl")
        val advertisements = listOf(
            downloadedAdvertisementOf(
                downloadUrl = adUrl,
                advertisement = advertisementOf(
                    duration = adDuration
                )
            )
        )
        val periodResult = periodResult(
            eventPayload = eventPayloadOf(
                burst = burstOf(
                    adUrl = adUrl.value
                )
            ),
            periods = listOf(
                periodOf(0..adDuration.milliseconds.toLong())
            )
        )
        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.Periodical,
            timeOf = Configuration.TimeOf.Advertisement,
            minimumListenTime = minimumListenTime,
        )

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results != null)
    }

    @Test
    fun testThatWhenStrategyIsOneOffAndAdvertisementAndReturnedPeriodsAreWithinAdvertisementDurationItReturnsValue() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val adDuration = 5.0.toDuration(DurationUnit.Seconds)
        val adUrl = Url("adUrl")
        val advertisements = listOf(
            downloadedAdvertisementOf(
                downloadUrl = adUrl,
                advertisement = advertisementOf(
                    duration = adDuration
                )
            )
        )
        val periodResult = periodResult(
            eventPayload = eventPayloadOf(
                burst = burstOf(
                    adUrl = adUrl.value
                )
            ),
            periods = listOf(
                periodOf(0..adDuration.milliseconds.toLong())
            )
        )
        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.OneOff,
            timeOf = Configuration.TimeOf.Advertisement,
            minimumListenTime = minimumListenTime,
        )

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results != null)
    }

    @Test
    fun testThatWhenStrategyIsOneOffAndBurstAndMultiplePeriodsReturnedAreWithinBurstDurationItReturnsValue() {
        // GIVEN
        val minimumListenTime = 5.0.toDuration(DurationUnit.Seconds)
        val advertisements = emptyList<DownloadedAdvertisement>()
        val periodResult = periodResult(
            eventPayload = eventPayloadOf(
                burst = burstOf(
                    duration = 5.0.toDuration(DurationUnit.Seconds)
                )
            ),
            periods = listOf(
                periodOf(longRange = 0L..1000L),
                periodOf(longRange = 1000L..2000L),
                periodOf(longRange = 2000L..3000L),
                periodOf(longRange = 3000L..4000L),
                periodOf(longRange = 4000L..5000L),
            )
        )
        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.OneOff,
            timeOf = Configuration.TimeOf.Burst,
            minimumListenTime = minimumListenTime,
        )

        // WHEN
        val results = listenedMediaStrategyOf(
            configuration = strategy
        ).check(periodResult, advertisements)

        // THEN
        assertTrue(results != null)
    }

    @Test
    fun testThatWhenStrategyIsPeriodicalAndBurstWeAreEvaluatingListOfPeriodsThenMultipleValuesCanBeReturned() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val advertisements = emptyList<DownloadedAdvertisement>()
        val periods = listOf(
            periodOf(longRange = 0L..1000L),
            periodOf(longRange = 1000L..2000L),
            periodOf(longRange = 2000L..3000L),
            periodOf(longRange = 3000L..4000L),
            periodOf(longRange = 4000L..5000L),
        )
            .map { listOf(it) }
            .scan(emptyList<PlaybackPeriodsCreator.Period>()) { acc, list -> acc + list }

        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.Periodical,
            timeOf = Configuration.TimeOf.Burst,
            minimumListenTime = minimumListenTime,
        )

        val periodResults = periods.map {
            periodResult(
                eventPayload = eventPayloadOf(
                    burst = burstOf(
                        duration = 5.0.toDuration(DurationUnit.Seconds)
                    )
                ),
                periods = it
            )
        }

        // WHEN
        val listenedMediaStrategy = listenedMediaStrategyOf(configuration = strategy)
        val results = periodResults.mapNotNull {
            listenedMediaStrategy.check(it, advertisements)
        }

        // THEN
        assertEquals(periods.size - 1, results.size)
    }

    @Test
    fun testThatWhenStrategyIsOneOffAndBurstWeAreEvaluatingListOfPeriodsThenOnlyOneValueCanBeReturned() {
        // GIVEN
        val minimumListenTime = 1.0.toDuration(DurationUnit.Seconds)
        val advertisements = emptyList<DownloadedAdvertisement>()
        val periods = listOf(
            periodOf(longRange = 0L..1000L),
            periodOf(longRange = 1000L..2000L),
            periodOf(longRange = 2000L..3000L),
            periodOf(longRange = 3000L..4000L),
            periodOf(longRange = 4000L..5000L),
        )
            .map { listOf(it) }
            .scan(emptyList<PlaybackPeriodsCreator.Period>()) { acc, list -> acc + list }

        val strategy = listenedMediaStrategyConfigurationOf(
            type = Configuration.Type.OneOff,
            timeOf = Configuration.TimeOf.Burst,
            minimumListenTime = minimumListenTime,
        )

        val periodResults = periods.map {
            periodResult(
                eventPayload = eventPayloadOf(
                    burst = burstOf(
                        duration = 5.0.toDuration(DurationUnit.Seconds)
                    )
                ),
                periods = it
            )
        }

        // WHEN
        val listenedMediaStrategy = listenedMediaStrategyOf(configuration = strategy)
        val results = periodResults.mapNotNull {
            listenedMediaStrategy.check(it, advertisements)
        }

        // THEN
        assertEquals(1, results.size)
    }
}

internal fun periodResult(
    eventPayload: EventPayload = eventPayloadOf(),
    periods: List<PlaybackPeriodsCreator.Period> = emptyList(),
): PlaybackPeriodsCreator.Result =
    PlaybackPeriodsCreator.Result(
        eventPayload = eventPayload,
        periods = periods,
    )

internal fun periodOf(
    longRange: LongRange = LongRange.EMPTY,
): PlaybackPeriodsCreator.Period =
    PlaybackPeriodsCreator.Period(longRange)

internal fun listenedMediaStrategyConfigurationOf(
    type: Configuration.Type = Configuration.Type.OneOff,
    timeOf: Configuration.TimeOf = Configuration.TimeOf.Burst,
    minimumListenTime: Duration = 1.0.toDuration(DurationUnit.Seconds),
): Configuration =
    Configuration(
        type = type,
        timeOf = timeOf,
        minimumListenTime = minimumListenTime,
    )

internal fun listenedMediaStrategyOf(
    refreshInterval: Duration = 1.0.toDuration(DurationUnit.Seconds),
    configuration: Configuration = listenedMediaStrategyConfigurationOf(),
): ListenedMediaStrategy =
    listenedMediaStrategyFactoryOf(
        refreshInterval = refreshInterval
    ).create(configuration)