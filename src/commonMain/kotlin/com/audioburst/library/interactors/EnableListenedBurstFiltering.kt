package com.audioburst.library.interactors

import com.audioburst.library.data.storage.UserStorage

internal class EnableListenedBurstFiltering(
    private val userStorage: UserStorage,
) {

    operator fun invoke(filterListenedBursts: Boolean) {
        userStorage.filterListenedBursts = filterListenedBursts
    }
}