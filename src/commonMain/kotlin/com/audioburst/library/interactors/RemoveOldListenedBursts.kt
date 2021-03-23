package com.audioburst.library.interactors

import com.audioburst.library.data.storage.ListenedBurstStorage

internal class RemoveOldListenedBursts(
    private val listenedBurstStorage: ListenedBurstStorage,
) {

    suspend operator fun invoke() {
        listenedBurstStorage.removeExpiredListenedBursts()
    }
}