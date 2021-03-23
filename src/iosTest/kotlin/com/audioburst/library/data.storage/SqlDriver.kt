package com.audioburst.library.data.storage

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual fun driver(schema: SqlDriver.Schema, name: String): SqlDriver = NativeSqliteDriver(schema, name)