package com.audioburst.library.models

expect class DateTime {

    fun toIsoDateString(): String

    fun isBefore(dateTime: DateTime): Boolean

    fun isAfter(dateTime: DateTime): Boolean

    fun plusDays(days: Long): DateTime

    fun minusDays(days: Long): DateTime

    companion object {
         fun now(): DateTime

         fun from(isoDateString: String): DateTime?
    }
}