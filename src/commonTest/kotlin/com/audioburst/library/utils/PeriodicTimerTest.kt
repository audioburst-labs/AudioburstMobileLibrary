package com.audioburst.library.utils

import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import com.audioburst.library.models.DurationUnit
import com.audioburst.library.models.toDuration
import com.audioburst.library.runTest
import com.audioburst.library.testCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlin.test.Test
import kotlin.test.assertEquals

class PeriodicTimerTest {

    private val scope = CoroutineScope(testCoroutineContext)
    private suspend fun test(timer: PeriodicTimer, validate: suspend FlowTurbine<PeriodicTimer.Tick>.() -> Unit) {
        timer.timer.test {
            validate()
        }
    }

    @Test
    fun `test that there is one expectedItem when we wait the same time as interval`() = runTest {
        val interval = 100.0.toDuration(DurationUnit.Milliseconds)
        val timer = PeriodicTimer(interval = interval, scope = scope)
        test(timer) {
            timer.start()
            delay(timeMillis = interval.milliseconds.toLong())
            timer.pause()
            assertEquals(PeriodicTimer.Tick, expectItem())
        }
    }

    @Test
    fun `test that there are two expectedItems when we wait double the interval time`() = runTest {
        val interval = 100.0.toDuration(DurationUnit.Milliseconds)
        val timer = PeriodicTimer(interval = interval, scope = scope)
        val expectedItems = 2
        test(timer) {
            timer.start()
            delay(timeMillis = interval.milliseconds.toLong() * expectedItems + MINIMAL_OFFSET)
            timer.pause()
            repeat(times = expectedItems) {
                assertEquals(PeriodicTimer.Tick, expectItem())
            }
        }
    }

    @Test
    fun `test that calling start multiple times doesn't result in multiple events`() = runTest {
        val interval = 100.0.toDuration(DurationUnit.Milliseconds)
        val timer = PeriodicTimer(interval = interval, scope = scope)
        test(timer) {
            timer.start()
            timer.start()
            timer.start()
            delay(timeMillis = interval.milliseconds.toLong())
            timer.pause()
            assertEquals(PeriodicTimer.Tick, expectItem())
        }
    }

    companion object {
        private const val MINIMAL_OFFSET = 20L
    }
}