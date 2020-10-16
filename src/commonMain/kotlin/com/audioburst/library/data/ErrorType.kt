package com.audioburst.library.data

import io.ktor.client.features.*
import io.ktor.utils.io.errors.*

sealed class ErrorType {

    class ConnectionError(val ioException: IOException) : ErrorType()

    class UnexpectedException(val throwable: Throwable) : ErrorType()

    sealed class HttpError : ErrorType() {

        object BadRequestException : HttpError()
        object UnauthorizedException : HttpError()
        object ForbiddenException : HttpError()
        object NotFoundException : HttpError()
        object InternalServerErrorException : HttpError()
        class UnexpectedException(val responseException: ResponseException) : HttpError()

        companion object {
            fun handle(responseException: ResponseException): HttpError =
                when (responseException.response.status.value) {
                    400 -> BadRequestException
                    401 -> UnauthorizedException
                    403 -> ForbiddenException
                    404 -> NotFoundException
                    in 500..599 -> InternalServerErrorException
                    else -> UnexpectedException(responseException)
                }
        }
    }

    companion object {
        fun createFrom(throwable: Throwable): ErrorType =
            when (throwable) {
                is ResponseException -> HttpError.handle(throwable)
                is IOException -> ConnectionError(throwable)
                else -> UnexpectedException(throwable)
            }
    }
}
