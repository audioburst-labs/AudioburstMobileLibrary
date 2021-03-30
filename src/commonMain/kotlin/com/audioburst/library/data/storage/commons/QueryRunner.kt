package com.audioburst.library.data.storage.commons

import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal interface QueryRunner {
    suspend fun <T> run(body: () -> T): T

    suspend fun <T> runTransaction(body: () -> T): T
}

internal class TransacterQueryRunner(
    private val transacter: Transacter,
    private val dispatcher: CoroutineDispatcher,
) : QueryRunner {

    override suspend fun <T> run(body: () -> T): T =
        withContext(dispatcher) {
            body()
        }

    override suspend fun <T> runTransaction(body: () -> T): T =
        withContext(dispatcher) {
            transacter.transactionWithResult {
                body()
            }
        }
}