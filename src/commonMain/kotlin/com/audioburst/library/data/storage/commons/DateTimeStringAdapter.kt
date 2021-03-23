package com.audioburst.library.data.storage.commons

import com.audioburst.library.models.DateTime
import com.squareup.sqldelight.ColumnAdapter

internal class DateTimeStringAdapter : ColumnAdapter<DateTime, String> {
    override fun decode(databaseValue: String): DateTime = DateTime.from(databaseValue)!!

    override fun encode(value: DateTime): String = value.toIsoDateString()
}