package com.audioburst.library.data.storage

import com.audioburst.Database
import com.audioburst.library.data.ListenedBurstModel
import com.audioburst.library.data.ListenedBurstModelQueries
import com.audioburst.library.data.storage.commons.DateTimeStringAdapter
import com.audioburst.library.data.storage.commons.QueryRunner
import com.squareup.sqldelight.db.SqlDriver
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

internal expect fun driver(schema: SqlDriver.Schema, name: String): SqlDriver

internal abstract class DatabaseTest {

    private var driver: SqlDriver? = null
    private var database: Database? = null

    protected val listenedBurstQueries: ListenedBurstModelQueries by lazy { database!!.listenedBurstModelQueries }

    @BeforeTest
    fun setup() {
        val dateTimeStringAdapter = DateTimeStringAdapter()

        driver = driver(
            schema = Database.Schema,
            name = "test.db"
        ).apply {
            Database.Schema.create(this)
        }

        database = Database(
            driver = driver!!,
            listenedBurstModelAdapter = ListenedBurstModel.Adapter(
                date_textAdapter = dateTimeStringAdapter,
            )
        )
    }

    @AfterTest
    fun cleanup() {
        driver!!.close()
        driver = null
        database = null
    }
}

internal fun queryRunnerOf(): QueryRunner = object : QueryRunner {
    override suspend fun <T> run(body: () -> T): T = body()
    override suspend fun <T> runTransaction(body: () -> T): T = body()
}