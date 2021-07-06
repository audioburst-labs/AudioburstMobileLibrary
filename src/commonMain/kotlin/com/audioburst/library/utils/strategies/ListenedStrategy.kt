package com.audioburst.library.utils.strategies

import com.audioburst.library.models.*
import com.audioburst.library.utils.Logger
import com.audioburst.library.utils.PlaybackPeriodsCreator
import com.audioburst.library.utils.strategies.ListenedMediaStrategy.Configuration

internal class ListenedStrategy(
    private val factory: ListenedMediaStrategy.Factory,
    private val creator: PlaybackPeriodsCreator,
) {

    private val creators = listOf(
        EventCreator(
            configuration = Configuration(
                type = Configuration.Type.Periodical,
                timeOf = Configuration.TimeOf.Burst,
                minimumListenTime = 10.0.toDuration(DurationUnit.Seconds)
            ),
            creator = { PlaybackEvent.Playing(it) }
        ),
        EventCreator(
            configuration = Configuration(
                type = Configuration.Type.OneOff,
                timeOf = Configuration.TimeOf.Burst,
                minimumListenTime = 2.0.toDuration(DurationUnit.Seconds)
            ),
            creator = { PlaybackEvent.TwoSecPlaying(it) }
        ),
        EventCreator(
            configuration = Configuration(
                type = Configuration.Type.OneOff,
                timeOf = Configuration.TimeOf.Advertisement,
                minimumListenTime = 2.0.toDuration(DurationUnit.Seconds)
            ),
            creator = { PlaybackEvent.TwoSecADPlaying(it) }
        ),
        EventCreator(
            configuration = Configuration(
                type = Configuration.Type.OneOff,
                timeOf = Configuration.TimeOf.Burst,
                minimumListenTime = 10.0.toDuration(DurationUnit.Seconds)
            ),
            creator = { PlaybackEvent.BurstListened(it) }
        ),
    )

    fun check(input: AnalysisInput): List<PlaybackEvent> {
        val results = creator.check(input)

        return results.flatMap { result ->
            creators.mapNotNull { creator ->
                creator.create(result, input)
            }
        }.apply {
            if (isNotEmpty()) {
                Logger.i("Detected: ${joinToString { it.actionName }}")
            }
        }
    }

    private inner class EventCreator(
        configuration: Configuration,
        private val creator: (EventPayload) -> PlaybackEvent,
    ) {
        private val listenedMediaStrategy = factory.create(configuration)
        fun create(result: PlaybackPeriodsCreator.Result, input: AnalysisInput): PlaybackEvent? =
            listenedMediaStrategy.check(result, input.advertisements)?.let(creator)
    }
}
