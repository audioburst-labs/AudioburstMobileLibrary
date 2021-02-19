package com.audioburst.library.data.repository.cache

internal interface Cache<KEY, VALUE> {

    suspend fun get(key: KEY): VALUE?

    suspend fun set(key: KEY, value: VALUE)
}