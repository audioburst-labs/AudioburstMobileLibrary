package com.audioburst.library.di.providers

import kotlinx.serialization.json.Json

internal class JsonProvider : Provider<Json> {

    override fun get(): Json =
        Json {
            ignoreUnknownKeys = true
            isLenient = false
            useAlternativeNames = false
        }
}
