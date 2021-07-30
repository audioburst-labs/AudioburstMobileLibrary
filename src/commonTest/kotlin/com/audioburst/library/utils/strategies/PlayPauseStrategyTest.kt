package com.audioburst.library.utils.strategies

import app.cash.turbine.test
import com.audioburst.library.interactors.eventPayloadOf
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.PlaybackEvent
import com.audioburst.library.models.toDuration
import com.audioburst.library.runTest
import kotlinx.coroutines.delay
import kotlin.test.BeforeTest
import kotlin.test.Test

class PlayPauseStrategyTest {

    private val minimumWaitTime = 100.0.toDuration(DurationUnit.Milliseconds)
    private val offset = 10.0.toDuration(DurationUnit.Milliseconds)
    private lateinit var strategy: PlayPauseStrategy

    @BeforeTest
    fun init() {
        strategy = PlayPauseStrategy(minimumWaitTime)
    }

    @Test
    fun `test if there is a call to pause without start before, there is nothing emitted`() = runTest {
        strategy.event.test {
            // GIVEN
            val payload = eventPayloadOf()

            // WHEN
            strategy.pause(payload)

            // THEN
            expectNoEvents()
        }
    }

    @Test
    fun `test if there is a call to play Start event is getting emitted`() = runTest {
        strategy.event.test {
            // GIVEN
            val payload = eventPayloadOf()

            // WHEN
            strategy.play(payload)

            // THEN
            require(awaitItem() is PlaybackEvent.Play)
        }
    }

    @Test
    fun `test that there are both Play and Pause emitted with timeout when there is a call to play and pause function`() = runTest {
        strategy.event.test {
            // GIVEN
            val payload = eventPayloadOf()

            // WHEN
            strategy.play(payload)
            strategy.pause(payload)
            delay(timeMillis = (minimumWaitTime + offset).milliseconds.toLong())

            // THEN
            require(awaitItem() is PlaybackEvent.Play)
            require(awaitItem() is PlaybackEvent.Pause)
        }
    }

    @Test
    fun `test that Play is emitted immediately when there was a longer break between Pause and Play`() = runTest {
        strategy.event.test {
            // GIVEN
            val payload = eventPayloadOf()

            // WHEN
            strategy.play(payload)
            strategy.pause(payload)
            delay(timeMillis = 2 * (minimumWaitTime + offset).milliseconds.toLong())
            strategy.play(payload)

            // THEN
            require(awaitItem() is PlaybackEvent.Play)
            require(awaitItem() is PlaybackEvent.Pause)
            require(awaitItem() is PlaybackEvent.Play)
        }
    }

    @Test
    fun `test that only one, first Play is emitted when calling play and pause functions quickly`() = runTest {
        strategy.event.test {
            // GIVEN
            val payload = eventPayloadOf()

            // WHEN
            strategy.play(payload)
            strategy.pause(payload)
            strategy.play(payload)

            // THEN
            require(awaitItem() is PlaybackEvent.Play)
        }
    }
}