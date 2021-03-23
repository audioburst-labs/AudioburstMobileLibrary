package com.audioburst.library.models

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

actual class DateTime private constructor(private val offsetDateTime: OffsetDateTime) {

    actual fun toIsoDateString(): String = offsetDateTime.format(offsetDateTimeFormatter)

    actual fun isBefore(dateTime: DateTime): Boolean = offsetDateTime.isBefore(dateTime.offsetDateTime)

    actual fun isAfter(dateTime: DateTime): Boolean = offsetDateTime.isAfter(dateTime.offsetDateTime)

    actual fun plusDays(days: Long): DateTime = DateTime(offsetDateTime.plusDays(days))

    actual fun minusDays(days: Long): DateTime = DateTime(offsetDateTime.minusDays(days))

    actual companion object {
        private val offsetDateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        actual fun now(): DateTime = DateTime(OffsetDateTime.now())

        actual fun from(isoDateString: String): DateTime? =
            try {
                OffsetDateTime.parse(isoDateString, offsetDateTimeFormatter)?.let(::DateTime)
            } catch (e: DateTimeParseException) {
                null
            }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DateTime

        if (offsetDateTime != other.offsetDateTime) return false

        return true
    }

    override fun hashCode(): Int {
        return offsetDateTime.hashCode()
    }

    override fun toString(): String {
        return offsetDateTime.toString()
    }
}