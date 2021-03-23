package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.repository.mappers.userStorageOf
import com.audioburst.library.data.storage.UnsentEventStorage
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.toDuration
import com.audioburst.library.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlaybackEventHandlerInteractorTest {

    private val listenedBurstStorage = InMemoryListenedBurstStorage()
    private val sentEvents = mutableListOf<MockUserRepository.SentEvent>()
    private val interactor = playbackEventHandlerInteractorOf(
        userRepository = userRepositoryOf(sentEvents = sentEvents),
        userStorage = userStorageOf(userId = ""),
        listenedBurstStorage = listenedBurstStorage,
    )

    @AfterTest
    fun clearSentEvents() {
        sentEvents.clear()
    }

    @Test
    fun testIfPlayerEventIsNotGettingSavedWhenThereIsAnNoErrorWhileSendingIt() = runTest {
        // GIVEN
        val playbackEvent = PlaybackEvent.StartOfPlay(eventPayloadOf())
        val userRepository: UserRepository = userRepositoryOf(
            returns = MockUserRepository.Returns(
                postPlayerEvent = Resource.Data(Unit),
            )
        )
        val unsentEventStorage: UnsentEventStorage = InMemoryUnsentEventStorage()

        // WHEN
        playbackEventHandlerInteractorOf(
            userStorage = userStorageOf(userId = ""),
            userRepository = userRepository,
            unsentEventStorage = unsentEventStorage,
        ).handle(playbackEvent)

        // THEN
        assertTrue(unsentEventStorage.getAllPlayerEvents().isEmpty())
    }

    @Test
    fun testIfPlayerEventIsGettingSavedWhenThereIsAnErrorWhileSendingIt() = runTest {
        // GIVEN
        val playbackEvent = PlaybackEvent.StartOfPlay(eventPayloadOf())
        val userRepository: UserRepository = userRepositoryOf(
            returns = MockUserRepository.Returns(
                postPlayerEvent = resourceErrorOf(),
            )
        )
        val unsentEventStorage: UnsentEventStorage = InMemoryUnsentEventStorage()

        // WHEN
        playbackEventHandlerInteractorOf(
            userStorage = userStorageOf(userId = ""),
            userRepository = userRepository,
            unsentEventStorage = unsentEventStorage,
        ).handle(playbackEvent)

        // THEN
        assertTrue(unsentEventStorage.getAllPlayerEvents().isNotEmpty())
    }

    @Test
    fun testIfPositionInBurstIs0IfEventIsStartOfPlay() = runTest {
        // GIVEN
        val playbackEvent = PlaybackEvent.StartOfPlay(
            eventPayloadOf(
                currentPlayBackPosition = 5.0.toDuration(DurationUnit.Seconds)
            )
        )

        // WHEN
        interactor.handle(playbackEvent)

        // THEN
        assertTrue(sentEvents.last().playerEvent.positionInBurst == 0.0)
    }

    @Test
    fun testIfPositionInBurstIsAccordingToCurrentPositionWhenEventHappenedWhilePlayingAd() = runTest {
        // GIVEN
        val currentPlayBackPosition = 2.0.toDuration(DurationUnit.Seconds)
        val playbackEvent = PlaybackEvent.Playing(
            eventPayloadOf(
                currentPlayBackPosition = currentPlayBackPosition,
                advertisement = advertisementOf(
                    duration = 5.0.toDuration(DurationUnit.Seconds),
                )
            )
        )

        // WHEN
        interactor.handle(playbackEvent)

        // THEN
        assertEquals(currentPlayBackPosition.seconds, sentEvents.last().playerEvent.positionInBurst)
    }

    @Test
    fun testIfPositionInBurstIsCalculatedAccordingToBurstWhenEventHappenedWhileNotPlayingAd() = runTest {
        // GIVEN
        val adDuration = 5.0.toDuration(DurationUnit.Seconds)
        val burstDuration = 4.0.toDuration(DurationUnit.Seconds)
        val currentPlayBackPosition = 8.0.toDuration(DurationUnit.Seconds)
        val playbackEvent = PlaybackEvent.Playing(
            eventPayloadOf(
                currentPlayBackPosition = currentPlayBackPosition,
                advertisement = advertisementOf(
                    duration = adDuration,
                ),
                burst = burstOf(
                    duration = burstDuration,
                )
            )
        )

        // WHEN
        interactor.handle(playbackEvent)

        // THEN
        assertEquals(currentPlayBackPosition.seconds-adDuration.seconds, sentEvents.last().playerEvent.positionInBurst)
    }

    @Test
    fun testIfTotalPlayTimeIs0IfEventIsStartOfPlay() = runTest {
        // GIVEN
        val playbackEvent = PlaybackEvent.StartOfPlay(
            eventPayloadOf(
                currentPlayBackPosition = 5.0.toDuration(DurationUnit.Seconds)
            )
        )

        // WHEN
        interactor.handle(playbackEvent)

        // THEN
        assertTrue(sentEvents.last().playerEvent.totalPlayTime == 0.0)
    }

    @Test
    fun testIfTotalPlayTimeIsAccordingToCurrentPositionWhenEventHappenedWhilePlayingAd() = runTest {
        // GIVEN
        val currentPlayBackPosition = 2.0.toDuration(DurationUnit.Seconds)
        val playbackEvent = PlaybackEvent.Playing(
            eventPayloadOf(
                currentPlayBackPosition = currentPlayBackPosition,
                advertisement = advertisementOf(
                    duration = 5.0.toDuration(DurationUnit.Seconds),
                )
            )
        )

        // WHEN
        interactor.handle(playbackEvent)

        // THEN
        assertEquals(currentPlayBackPosition.seconds, sentEvents.last().playerEvent.totalPlayTime)
    }

    @Test
    fun testIfTotalPlayTimeIsCalculatedAccordingToBurstWhenEventHappenedWhileNotPlayingAd() = runTest {
        // GIVEN
        val adDuration = 5.0.toDuration(DurationUnit.Seconds)
        val burstDuration = 4.0.toDuration(DurationUnit.Seconds)
        val currentPlayBackPosition = 8.0.toDuration(DurationUnit.Seconds)
        val playbackEvent = PlaybackEvent.Playing(
            eventPayloadOf(
                currentPlayBackPosition = currentPlayBackPosition,
                advertisement = advertisementOf(
                    duration = adDuration,
                ),
                burst = burstOf(
                    duration = burstDuration,
                )
            )
        )

        // WHEN
        interactor.handle(playbackEvent)

        // THEN
        assertEquals(currentPlayBackPosition.seconds-adDuration.seconds, sentEvents.last().playerEvent.totalPlayTime)
    }

    @Test
    fun testIfBurstLengthIsAccordingToBurstDurationIfEventIsStartOfPlay() = runTest {
        // GIVEN
        val burstDuration = 5.0.toDuration(DurationUnit.Seconds)
        val playbackEvent = PlaybackEvent.StartOfPlay(
            eventPayloadOf(
                burst = burstOf(
                    duration = burstDuration
                )
            )
        )

        // WHEN
        interactor.handle(playbackEvent)

        // THEN
        assertEquals(burstDuration.seconds, sentEvents.last().playerEvent.burstLength)
    }

    @Test
    fun testIfBurstLengthIsAccordingToBurstDurationWhenNotPlayingAd() = runTest {
        // GIVEN
        val burstDuration = 5.0.toDuration(DurationUnit.Seconds)
        val playbackEvent = PlaybackEvent.Playing(
            eventPayloadOf(
                burst = burstOf(
                    duration = burstDuration
                )
            )
        )

        // WHEN
        interactor.handle(playbackEvent)

        // THEN
        assertEquals(burstDuration.seconds, sentEvents.last().playerEvent.burstLength)
    }

    @Test
    fun testIfBurstLengthIsAccordingToAdvertisementDurationWhenPlayingAd() = runTest {
        // GIVEN
        val duration = 5.0.toDuration(DurationUnit.Seconds)
        val playbackEvent = PlaybackEvent.Playing(
            eventPayloadOf(
                advertisement = advertisementOf(
                    duration = duration
                )
            )
        )

        // WHEN
        interactor.handle(playbackEvent)

        // THEN
        assertEquals(duration.seconds, sentEvents.last().playerEvent.burstLength)
    }

    @Test
    fun testWhenListenedBurstEventHappenedThenSentEventsAreEmptyAndListenedBurstStorageGetAllIsNotEmpty() = runTest {
        // GIVEN
        val playbackEvent = PlaybackEvent.BurstListened(eventPayloadOf())

        // WHEN
        interactor.handle(playbackEvent)

        // THEN
        assertTrue(sentEvents.isEmpty())
        assertTrue(listenedBurstStorage.getRecentlyListened().isNotEmpty())
    }
}
