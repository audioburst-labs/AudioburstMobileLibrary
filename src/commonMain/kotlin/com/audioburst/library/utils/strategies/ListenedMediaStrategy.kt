package com.audioburst.library.utils.strategies

import com.audioburst.library.models.*
import com.audioburst.library.utils.*

internal class ListenedMediaStrategy private constructor(
    private val configuration: Configuration,
    private val refreshInterval: Long,
) {

    private var previousNotifyTime by atomic(0)
    private var previouslyCheckedBurstId by nullableAtomic<String>()

    fun check(periodsResult: PlaybackPeriodsCreator.Result, advertisements: List<DownloadedAdvertisement>): EventPayload? {
        if (periodsResult.isPeriodBeginning()) {
            previousNotifyTime = 0
            previouslyCheckedBurstId = null
        }
        return checkStrategy(periodsResult, advertisements)
    }

    /**
     * Adding 20% of [refreshInterval] to catch a case when Library started querying a [PlaybackState] a little after
     * player started to play. 
     */
    private fun PlaybackPeriodsCreator.Result.isPeriodBeginning(): Boolean =
        duration <= refreshInterval + (refreshInterval * PERIOD_BEGINNING_OFFSET_PERCENT)

    private fun checkStrategy(periodsResult: PlaybackPeriodsCreator.Result, advertisements: List<DownloadedAdvertisement>): EventPayload? {
        val allPeriods = periodsResult.periods
        val burst = periodsResult.eventPayload.burst

        val playTimeMs = when (configuration.timeOf) {
            Configuration.TimeOf.Burst -> burst.burstPlayTimeMs(periodsResult)
            Configuration.TimeOf.Advertisement -> burst.advertisementPlayTimeMs(periodsResult, advertisements)
            Configuration.TimeOf.Total -> allPeriods.sumOf { it.duration }
        }
        return when (configuration.type) {
            Configuration.Type.OneOff -> {
                if (playTimeMs >= configuration.minimumListenTime.milliseconds.toLong() && previouslyCheckedBurstId != burst.id) {
                    previouslyCheckedBurstId = burst.id
                    periodsResult.eventPayload
                } else {
                    null
                }
            }
            Configuration.Type.Periodical -> {
                val remainder = playTimeMs.rem(configuration.minimumListenTime.milliseconds.toLong())
                if (playTimeMs >= previousNotifyTime + refreshInterval && playTimeMs >= refreshInterval && remainder in 0..refreshInterval) {
                    previousNotifyTime = playTimeMs
                    periodsResult.eventPayload
                } else {
                    null
                }
            }
        }
    }

    private fun Burst.burstPlayTimeMs(periodsResult: PlaybackPeriodsCreator.Result): Long {
        val currentUrl = periodsResult.forUrl
        return if (audioUrl == currentUrl || streamUrl == currentUrl || source.audioUrl == currentUrl) periodsResult.duration else 0L
    }

    private fun Burst.advertisementPlayTimeMs(periodsResult: PlaybackPeriodsCreator.Result, advertisements: List<DownloadedAdvertisement>): Long {
        val advertisement = advertisement(advertisements) ?: return 0
        return if (advertisement.burstUrl == periodsResult.forUrl) periodsResult.duration else 0
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

    companion object {
        private const val PERIOD_BEGINNING_OFFSET_PERCENT = 0.2
    }
}