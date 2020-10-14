package com.audioburst.library.utils

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal interface JsonEncoder {

    fun encodeToString(value: Any): String
}

internal class SerializationJsonEncoder(private val json: Json) : JsonEncoder {

    override fun encodeToString(value: Any): String = json.encodeToString(value)
}
