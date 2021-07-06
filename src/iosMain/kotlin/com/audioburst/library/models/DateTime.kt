package com.audioburst.library.models

import platform.Foundation.*

actual class DateTime private constructor(private val nsDate: NSDate) {

    actual fun toIsoDateString(): String {
        val formatter = NSISO8601DateFormatter()
        return formatter.stringFromDate(nsDate)
    }

    actual fun isBefore(dateTime: DateTime): Boolean = nsDate.compare(dateTime.nsDate) == NSOrderedAscending

    actual fun isAfter(dateTime: DateTime): Boolean = nsDate.compare(dateTime.nsDate) == NSOrderedDescending

    actual fun plusDays(days: Long): DateTime =
        DateTime(
            NSCalendar.currentCalendar.dateByAddingUnit(
                unit = NSDayCalendarUnit,
                value = days,
                toDate = nsDate,
                options = 0,
            )!!
        )

    actual fun minusDays(days: Long): DateTime =
        DateTime(
            NSCalendar.currentCalendar.dateByAddingUnit(
                unit = NSDayCalendarUnit,
                value = -days,
                toDate = nsDate,
                options = 0,
            )!!
        )

    actual companion object {
        actual fun now(): DateTime = DateTime(NSDate())

        actual fun from(isoDateString: String): DateTime? {
            val formatter = NSISO8601DateFormatter()
            return formatter.dateFromString(isoDateString)?.let(::DateTime)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DateTime) return false

        if (nsDate != other.nsDate) return false

        return true
    }

    override fun hashCode(): Int {
        return nsDate.hashCode()
    }

    override fun toString(): String {
        return nsDate.toString()
    }
}