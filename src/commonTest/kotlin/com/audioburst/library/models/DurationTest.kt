package com.audioburst.library.models

import kotlin.test.Test
import kotlin.test.assertEquals

class DurationTest {

    @Test
    fun testSecondToMillisecondConversion() {
        // GIVEN
        val seconds = 5.43

        // WHEN
        val duration = seconds.toDuration(DurationUnit.Seconds)

        // THEN
        assertEquals(duration.seconds, seconds)
        assertEquals(duration.milliseconds, seconds * 1000)
    }

    @Test
    fun testMillisecondToSecondConversion() {
        // GIVEN
        val milliseconds = 234.34

        // WHEN
        val duration = milliseconds.toDuration(DurationUnit.Milliseconds)

        // THEN
        assertEquals(duration.milliseconds, milliseconds)
        assertEquals(duration.seconds, milliseconds / 1000)
    }
}
