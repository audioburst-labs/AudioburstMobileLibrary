package com.audioburst.library.data.storage

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual fun driver(schema: SqlDriver.Schema, name: String): SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)