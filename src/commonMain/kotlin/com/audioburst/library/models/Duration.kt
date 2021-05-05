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

    override fun toString(): String = "${milliseconds}ms"

    operator fun plus(other: Duration): Duration = (milliseconds + other.milliseconds).toDuration(DurationUnit.Milliseconds)

    operator fun minus(other: Duration): Duration = (milliseconds - other.milliseconds).toDuration(DurationUnit.Milliseconds)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Duration

        return milliseconds == other.milliseconds
    }

    override fun hashCode(): Int {
        return milliseconds.hashCode()
    }

    operator fun compareTo(threshold: Duration): Int = milliseconds.compareTo(threshold.milliseconds)

    companion object {
        private const val SECONDS_TO_MILLISECONDS_MULTIPLIER = 1000
    }
}

fun Double.toDuration(unit: DurationUnit): Duration = Duration(this, unit)

enum class DurationUnit {
    Seconds, Milliseconds
}
