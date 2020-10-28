package com.audioburst.library.data.storage

import com.audioburst.library.models.AdvertisementEvent
import com.audioburst.library.models.PlayerEvent

internal interface UnsentEventStorage {

    suspend fun add(advertisementEvent: AdvertisementEvent)

    suspend fun remove(advertisementEvent: AdvertisementEvent)

    suspend fun getAllAdvertisementEvents(): List<AdvertisementEvent>

    suspend fun add(playerEvent: PlayerEvent)

    suspend fun remove(playerEvent: PlayerEvent)

    suspend fun getAllPlayerEvents(): List<PlayerEvent>
}

/**
 * This class is going to be implemented in ABU-695
 */
internal class NoOpUnsentEventStorage : UnsentEventStorage {
    override suspend fun add(advertisementEvent: AdvertisementEvent) = Unit
    override suspend fun add(playerEvent: PlayerEvent) = Unit
    override suspend fun remove(advertisementEvent: AdvertisementEvent) = Unit
    override suspend fun remove(playerEvent: PlayerEvent) = Unit
    override suspend fun getAllAdvertisementEvents(): List<AdvertisementEvent> = emptyList()
    override suspend fun getAllPlayerEvents(): List<PlayerEvent> = emptyList()
}
