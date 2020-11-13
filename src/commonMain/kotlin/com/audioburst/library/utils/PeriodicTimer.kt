package com.audioburst.library.utils

import co.touchlab.stately.concurrency.AtomicBoolean
import co.touchlab.stately.concurrency.AtomicReference
import com.audioburst.library.models.Duration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

@OptIn(ExperimentalCoroutinesApi::class)
internal class PeriodicTimer {

    private var currentProducerScope: AtomicReference<ProducerScope<Result>?> = AtomicReference(null)
    private var isRunning: AtomicBoolean = AtomicBoolean(false)

    fun start(interval: Duration): Flow<Result> =
        channelFlow {
            if (isRunning.value) {
                send(Result.AlreadyRunning)
            } else {
                currentProducerScope.set(this)
                isRunning.value = true
                while (isRunning.value) {
                    delay(interval.milliseconds.toLong())
                    if (!isClosedForSend) {
                        send(Result.OnTick)
                    }
                }
            }
        }

    fun pause(): Boolean =
        if (isRunning.value) {
            isRunning.value = false
            currentProducerScope.get()?.close()
            true
        } else {
            false
        }

    sealed class Result {
        object OnTick: Result()
        object AlreadyRunning: Result()
    }
}
