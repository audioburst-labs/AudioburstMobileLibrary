package com.audioburst.library.models

import com.audioburst.library.utils.Strings

sealed class Result<out T> {

    data class Data<out T>(val value: T) : Result<T>()
    data class Error(val error: LibraryError) : Result<Nothing>()
}

enum class LibraryError(val message: String) {
    Network(Strings.errorNetwork),
    Server(Strings.errorServer),
    Unexpected(Strings.errorUnexpected),
    WrongApplicationKey(Strings.errorWrongApplicationKey),
    AdUrlNotFound(Strings.errorAdUrlNotFound),
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
