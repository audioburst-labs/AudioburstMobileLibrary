package com.audioburst.library.data.storage

import com.audioburst.library.data.ListenedBurstModel
import com.audioburst.library.data.ListenedBurstModelQueries
import com.audioburst.library.data.storage.commons.QueryRunner
import com.audioburst.library.models.ListenedBurst

internal interface ListenedBurstStorage {

    suspend fun getRecentlyListened(): List<ListenedBurst>

    suspend fun addOrUpdate(listenedBurst: ListenedBurst)

    suspend fun removeExpiredListenedBursts()
}

internal class SqlListenedBurstStorage(
    private val queryRunner: QueryRunner,
    private val listenedBurstQueries: ListenedBurstModelQueries,
) : ListenedBurstStorage {

    override suspend fun getRecentlyListened(): List<ListenedBurst> = queryRunner.run {
        listenedBurstQueries.selectAllFromLast30Days().executeAsList().map(ListenedBurstModel::asListenedBurst)
    }

    override suspend fun addOrUpdate(listenedBurst: ListenedBurst) = queryRunner.run {
        listenedBurstQueries.insertListenedBurst(
            id = listenedBurst.id,
            date_text = listenedBurst.dateTime,
        )
    }

    override suspend fun removeExpiredListenedBursts() = queryRunner.run {
        listenedBurstQueries.deleteExpiredListenedBurst()
    }
}

private fun ListenedBurstModel.asListenedBurst(): ListenedBurst =
    ListenedBurst(id = id, dateTime = date_text)