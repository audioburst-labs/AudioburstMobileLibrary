package com.audioburst.library.interactors

import com.audioburst.library.data.Resource
import com.audioburst.library.data.repository.UserRepository
import com.audioburst.library.data.repository.mappers.userStorageOf
import com.audioburst.library.data.storage.UnsentEventStorage
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class PlaybackEventHandlerInteractorTest {

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
}
