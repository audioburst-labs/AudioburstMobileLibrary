package com.audioburst.library.models

import com.audioburst.library.utils.Strings

sealed class Result<out T> {

    class Data<out T>(val value: T) : Result<T>() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Data<*>

            if (value != other.value) return false

            return true
        }

        override fun hashCode(): Int {
            return value?.hashCode() ?: 0
        }

        override fun toString(): String {
            return "Data(value=$value)"
        }
    }

    class Error(val error: LibraryError) : Result<Nothing>() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Error

            if (error != other.error) return false

            return true
        }

        override fun hashCode(): Int {
            return error.hashCode()
        }

        override fun toString(): String {
            return "Error(error=$error)"
        }
    }
}

enum class LibraryError(val message: String) {
    Network(Strings.ERROR_NETWORK),
    Server(Strings.ERROR_SERVER),
    Unexpected(Strings.ERROR_UNEXPECTED),
    WrongApplicationKey(Strings.ERROR_WRONG_APPLICATION_KEY),
    AdUrlNotFound(Strings.ERROR_AD_URL_NOT_FOUND),
    NoKeysSelected(Strings.ERROR_NO_KEYS_SELECTED),
    NoSearchResults(Strings.ERROR_NO_SEARCH_RESULTS),
}

inline fun <T, U> Result<T>.map(f: (T) -> U): Result<U> = when (this) {
    is Result.Data -> Result.Data(f(value))
    is Result.Error -> this
}

inline fun <T> Result<T>.onData(f: (T) -> Unit): Result<T> = when (this) {
    is Result.Data -> {
        f(value)
        this
    }
    is Result.Error -> this
}

inline fun <T> Result<T>.onError(f: (LibraryError) -> Unit): Result<T> = when (this) {
    is Result.Data -> this
    is Result.Error -> {
        f(error)
        this
    }
}

val <T> Result<T>.value: T?
    get() = when (this) {
        is Result.Data -> value
        is Result.Error -> null
    }

val Result<*>.errorType: LibraryError?
    get() = when (this) {
        is Result.Data -> null
        is Result.Error -> this.error
    }
