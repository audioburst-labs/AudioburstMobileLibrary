package com.audioburst.library.utils.strategies

import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.concurrency.value
import com.audioburst.library.models.*
import com.audioburst.library.models.DownloadedAdvertisement
import com.audioburst.library.models.EventPayload
import com.audioburst.library.utils.PlaybackPeriodsCreator

internal class ListenedMediaStrategy private constructor(
        private val configuration: Configuration,
        private val refreshInterval: Long,
) {

    private val previousNotifyTime: AtomicReference<Long> = AtomicReference(0)
    private val previouslyCheckedBurstId: AtomicReference<String?> = AtomicReference(null)

    fun check(periodsResult: PlaybackPeriodsCreator.Result, advertisements: List<DownloadedAdvertisement>): EventPayload? {
        if (periodsResult.duration <= refreshInterval) {
            previousNotifyTime.value = 0
            previouslyCheckedBurstId.value = null
        }
        return checkStrategy(periodsResult, advertisements)
    }

    private fun checkStrategy(periodsResult: PlaybackPeriodsCreator.Result, advertisements: List<DownloadedAdvertisement>): EventPayload? {
        val allPeriods = periodsResult.periods
        val burst = periodsResult.eventPayload.burst

        val playTimeMs = when (configuration.timeOf) {
            Configuration.TimeOf.Burst -> burst.burstRange(advertisements).countIfPresentInRanges(allPeriods.map { it.range })
            Configuration.TimeOf.Advertisement -> burst.advertisementRange(advertisements)?.countIfPresentInRanges(allPeriods.map { it.range }) ?: 0
            Configuration.TimeOf.Total -> allPeriods.sumOf { it.duration }
        }
        return when (configuration.type) {
            Configuration.Type.OneOff -> {
                if (playTimeMs >= configuration.minimumListenTime.milliseconds.toLong() && previouslyCheckedBurstId.value != burst.id) {
                    previouslyCheckedBurstId.value = burst.id
                    periodsResult.eventPayload
                } else {
                    null
                }
            }
            Configuration.Type.Periodical -> {
                val remainder = playTimeMs.rem(configuration.minimumListenTime.milliseconds.toLong())
                if (playTimeMs > previousNotifyTime.value + refreshInterval && playTimeMs > refreshInterval && remainder in 0 until refreshInterval) {
                    previousNotifyTime.value = playTimeMs
                    periodsResult.eventPayload
                } else {
                    null
                }
            }
        }
    }

    private fun LongRange.countIfPresentInRanges(ranges: List<LongRange>): Long {
        var sum = 0L
        forEach { value ->
            ranges.forEach {
                if (it.contains(value)) {
                    sum++
                }
            }
        }
        return sum
    }

    private fun Burst.burstRange(advertisements: List<DownloadedAdvertisement>): LongRange =
        advertisement(advertisements)?.let {
            LongRange(start = it.duration.milliseconds.toLong(), endInclusive = duration.milliseconds.toLong())
        } ?: LongRange(start = 0, endInclusive = duration.milliseconds.toLong())

    private fun Burst.advertisementRange(advertisements: List<DownloadedAdvertisement>): LongRange? =
        advertisement(advertisements)?.let {
            LongRange(start = 0, endInclusive = it.duration.milliseconds.toLong())
        }

    internal class Factory(private val refreshInterval: Duration) {
        fun create(configuration: Configuration): ListenedMediaStrategy =
            ListenedMediaStrategy(
                configuration = configuration,
                refreshInterval = refreshInterval.milliseconds.toLong(),
            )
    }

    data class Configuration(
        val type: Type,
        val timeOf: TimeOf,
        val minimumListenTime: Duration,
    ) {

        enum class TimeOf {
            Burst, Advertisement, Total
        }

        enum class Type {
            OneOff, Periodical
        }
    }
}