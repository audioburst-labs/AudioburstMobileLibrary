package com.audioburst.library.utils

import com.squareup.sqldelight.db.SqlDriver

internal expect fun driver(databaseName: String): SqlDriver