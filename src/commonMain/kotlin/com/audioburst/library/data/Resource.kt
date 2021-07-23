package com.audioburst.library.data

import com.audioburst.library.models.LibraryError
import com.audioburst.library.models.Result

internal sealed class Resource<out T> {
    data class Data<out T>(val result: T) : Resource<T>()
    data class Error(val errorType: ErrorType) : Resource<Nothing>()
}

internal inline infix fun <T, U> Resource<T>.then(f: (T) -> Resource<U>): Resource<U> = when (this) {
    is Resource.Data -> f(result)
    is Resource.Error -> this
}

internal inline fun <T, U> Resource<T>.map(f: (T) -> U): Resource<U> = when (this) {
    is Resource.Data -> Resource.Data(f(result))
    is Resource.Error -> this
}

internal inline fun <T> Resource<T>.mapErrorToData(default: () -> T): Resource.Data<T> = when (this) {
    is Resource.Data -> this
    is Resource.Error -> Resource.Data(default())
}

internal inline infix fun <T> Resource<T>.onData(f: (T) -> Unit): Resource<T> = when (this) {
    is Resource.Data -> {
        f(result)
        this
    }
    is Resource.Error -> this
}

internal inline infix fun Resource<*>.onError(f: (ErrorType) -> Unit): Resource<*> = when (this) {
    is Resource.Data -> this
    is Resource.Error -> {
        f(errorType)
        this
    }
}

internal fun <T> Resource<T>.result(): T? = when (this) {
    is Resource.Data -> result
    is Resource.Error -> null
}

internal fun <T> Resource<T>.asResult(): Result<T> =
    when (this) {
        is Resource.Data -> Result.Data(result)
        is Resource.Error -> Result.Error(errorType.toLibraryError())
    }

internal fun ErrorType.toLibraryError(): LibraryError =
    when (this) {
        is ErrorType.ConnectionError -> LibraryError.Network
        ErrorType.HttpError.UnauthorizedException -> LibraryError.WrongApplicationKey
        ErrorType.HttpError.BadRequestException, ErrorType.HttpError.ForbiddenException,
        ErrorType.HttpError.NotFoundException, ErrorType.HttpError.InternalServerErrorException -> LibraryError.Server
        is ErrorType.HttpError.UnexpectedException, is ErrorType.UnexpectedException -> LibraryError.Unexpected
    }
