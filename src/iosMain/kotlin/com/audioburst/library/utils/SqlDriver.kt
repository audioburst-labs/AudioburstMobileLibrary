package com.audioburst.library.utils

import com.audioburst.Database
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

internal actual fun driver(databaseName: String): SqlDriver =
    NativeSqliteDriver(
        schema = Database.Schema,
        name = databaseName,
    )