package com.audioburst.library.data.storage.commons

import com.audioburst.library.models.AppDispatchers
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.withContext

internal interface QueryRunner {
    suspend fun <T> run(body: () -> T): T

    suspend fun <T> runTransaction(body: () -> T): T
}

internal class TransacterQueryRunner(
    private val transacter: Transacter,
    private val appDispatchers: AppDispatchers,
) : QueryRunner {

    override suspend fun <T> run(body: () -> T): T =
        withContext(appDispatchers.computation) {
            body()
        }

    override suspend fun <T> runTransaction(body: () -> T): T =
        withContext(appDispatchers.computation) {
            transacter.transactionWithResult {
                body()
            }
        }
}