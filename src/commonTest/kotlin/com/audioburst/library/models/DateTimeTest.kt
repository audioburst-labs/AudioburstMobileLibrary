package com.audioburst.library.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DateTimeTest {

    @Test
    fun testIfDateTimeIsBeingCreatedWhenParsingCorrectIsoDateString() {
        // GIVEN
        val isoDateString = "2016-08-26T12:39:00Z"

        // WHEN
        val dateTime = DateTime.from(isoDateString)

        // THEN
        assertTrue(dateTime != null)
    }

    @Test
    fun testIfDateTimeIsNotBeingCreatedWhenParsingWrongIsoDateString() {
        // GIVEN
        val isoDateString = "2016-08-26"

        // WHEN
        val dateTime = DateTime.from(isoDateString)

        // THEN
        assertTrue(dateTime == null)
    }

    @Test
    fun testIfDateTimeIsNotBeingCreatedWhenParsingEmptyString() {
        // GIVEN
        val isoDateString = ""

        // WHEN
        val dateTime = DateTime.from(isoDateString)

        // THEN
        assertTrue(dateTime == null)
    }

    @Test
    fun testIfTwoDateTimeAreEqualsWhenCreatedFromTheSameString() {
        // GIVEN
        val isoDateString = "2016-08-26T12:39:00Z"

        // WHEN
        val dateTime1 = DateTime.from(isoDateString)
        val dateTime2 = DateTime.from(isoDateString)

        // THEN
        assertEquals(dateTime2, dateTime1)
    }

    @Test
    fun testIfParsingFromStringAndBackIsWorkingProperly() {
        // GIVEN
        val isoDateString = "2016-08-26T12:39:00Z"

        // WHEN
        val dateTime = DateTime.from(isoDateString)
        val parsed = dateTime?.toIsoDateString()

        // THEN
        assertEquals(isoDateString, parsed)
    }

    @Test
    fun testIfAddingDaysWorkingProperly() {
        // GIVEN
        val daysToAdd = 3L
        val date = DateTime.from(isoDateString = "2016-08-26T12:39:00Z")!!
        val expectedDate = DateTime.from(isoDateString = "2016-08-29T12:39:00Z")!!

        // WHEN
        val dateTime = date.plusDays(daysToAdd)

        // THEN
        assertEquals(expectedDate, dateTime)
    }

    @Test
    fun testIfSubtractingDaysWorkingProperly() {
        // GIVEN
        val daysToAdd = 3L
        val date = DateTime.from(isoDateString = "2016-08-26T12:39:00Z")!!
        val expectedDate = DateTime.from(isoDateString = "2016-08-23T12:39:00Z")!!

        // WHEN
        val dateTime = date.minusDays(daysToAdd)

        // THEN
        assertEquals(expectedDate, dateTime)
    }

    @Test
    fun testIfAddingDaysWorkingProperlyWhenCrossingMonth() {
        // GIVEN
        val daysToAdd = 8L
        val date = DateTime.from(isoDateString = "2016-08-26T12:39:00Z")!!
        val expectedDate = DateTime.from(isoDateString = "2016-09-03T12:39:00Z")!!

        // WHEN
        val dateTime = date.plusDays(daysToAdd)

        // THEN
        assertEquals(expectedDate, dateTime)
    }

    @Test
    fun testIfSubtractingDaysWorkingProperlyWhenCrossingMonth() {
        // GIVEN
        val daysToAdd = 28L
        val date = DateTime.from(isoDateString = "2016-08-26T12:39:00Z")!!
        val expectedDate = DateTime.from(isoDateString = "2016-07-29T12:39:00Z")!!

        // WHEN
        val dateTime = date.minusDays(daysToAdd)

        // THEN
        assertEquals(expectedDate, dateTime)
    }

    @Test
    fun testIfDateIsDetectedAsBefore() {
        // GIVEN
        val date = DateTime.from(isoDateString = "2016-08-26T12:39:00Z")!!
        val fartherDate = DateTime.from(isoDateString = "2016-09-03T12:39:00Z")!!

        // WHEN
        val isBefore = date.isBefore(fartherDate)

        // THEN
        assertTrue(isBefore)
    }

    @Test
    fun testIfDateIsDetectedAsAfter() {
        // GIVEN
        val date = DateTime.from(isoDateString = "2016-08-26T12:39:00Z")!!
        val fartherDate = DateTime.from(isoDateString = "2016-09-03T12:39:00Z")!!

        // WHEN
        val isAfter = fartherDate.isAfter(date)

        // THEN
        assertTrue(isAfter)
    }

    @Test
    fun testIfDateIsDetectedAsNotBefore() {
        // GIVEN
        val date = DateTime.from(isoDateString = "2016-08-26T12:39:00Z")!!
        val fartherDate = DateTime.from(isoDateString = "2016-09-03T12:39:00Z")!!

        // WHEN
        val isBefore = fartherDate.isBefore(date)

        // THEN
        assertTrue(!isBefore)
    }

    @Test
    fun testIfDateIsDetectedAsNotAfter() {
        // GIVEN
        val date = DateTime.from(isoDateString = "2016-08-26T12:39:00Z")!!
        val fartherDate = DateTime.from(isoDateString = "2016-09-03T12:39:00Z")!!

        // WHEN
        val isAfter = date.isAfter(fartherDate)

        // THEN
        assertTrue(!isAfter)
    }
}