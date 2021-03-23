package com.audioburst.library.utils

import com.audioburst.Database
import com.audioburst.library.applicationContext
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

internal actual fun driver(databaseName: String): SqlDriver =
    AndroidSqliteDriver(
        schema = Database.Schema,
        name = databaseName,
        context = applicationContext,
    )