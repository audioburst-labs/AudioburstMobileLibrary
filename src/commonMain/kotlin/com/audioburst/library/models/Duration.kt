package com.audioburst.library.models

class Duration(private val value: Double, private val unit: DurationUnit) {

    val seconds: Double
        get() = convert(
            value = value,
            from = unit,
            to = DurationUnit.Seconds
        )

    val milliseconds: Double
        get() = convert(
            value = value,
            from = unit,
            to = DurationUnit.Milliseconds
        )

    private fun convert(value: Double, from: DurationUnit, to: DurationUnit): Double =
        when (from) {
            DurationUnit.Seconds -> when (to) {
                DurationUnit.Seconds -> value
                DurationUnit.Milliseconds -> value * SECONDS_TO_MILLISECONDS_MULTIPLIER
            }
            DurationUnit.Milliseconds -> when (to) {
                DurationUnit.Seconds -> value / SECONDS_TO_MILLISECONDS_MULTIPLIER
                DurationUnit.Milliseconds -> value
            }
        }

    companion object {
        private const val SECONDS_TO_MILLISECONDS_MULTIPLIER = 1000
    }
}

fun Double.toDuration(unit: DurationUnit): Duration = Duration(this, unit)

enum class DurationUnit {
    Seconds, Milliseconds
}
