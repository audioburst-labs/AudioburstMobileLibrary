package com.audioburst.library.utils

import com.audioburst.library.models.Duration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow

@OptIn(ExperimentalCoroutinesApi::class)
internal class PeriodicTimer {

    private var currentProducerScope by atomic<ProducerScope<Result>?>(null)
    private var isRunning by atomic(false)

    fun start(interval: Duration): Flow<Result> =
        channelFlow {
            if (isRunning) {
                send(Result.AlreadyRunning)
            } else {
                currentProducerScope = this
                isRunning = true
                while (isRunning) {
                    delay(interval.milliseconds.toLong())
                    if (!isClosedForSend) {
                        send(Result.OnTick)
                    }
                }
            }
        }

    fun pause(): Boolean =
        if (isRunning) {
            isRunning = false
            currentProducerScope?.close()
            true
        } else {
            false
        }

    sealed class Result {
        object OnTick: Result()
        object AlreadyRunning: Result()
    }
}
