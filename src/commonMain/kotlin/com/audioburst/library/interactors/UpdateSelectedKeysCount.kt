package com.audioburst.library.interactors

import com.audioburst.library.data.storage.UserStorage
import com.audioburst.library.models.UserPreferences

internal class UpdateSelectedKeysCount(
    private val userStorage: UserStorage,
) {

    operator fun invoke(userPreferences: UserPreferences) {
        userStorage.selectedKeysCount = userPreferences
            .preferences
            .flatMap { it.keys }
            .filter { it.selected }
            .size
    }
}