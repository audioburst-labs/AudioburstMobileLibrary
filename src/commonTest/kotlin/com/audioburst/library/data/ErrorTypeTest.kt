package com.audioburst.library.data

import com.audioburst.library.models.LibraryError
import io.ktor.client.call.*
import io.ktor.client.features.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.date.*
import io.ktor.utils.io.*
import io.ktor.utils.io.errors.*
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ErrorTypeTest {

    @Test
    fun testIfErrorIsConnectionErrorWhenThereIsAnIoexception() {
        // GIVEN
        val response = IOException("")

        // WHEN
        val error = ErrorType.createFrom(response)

        // THEN
        assertTrue(error is ErrorType.ConnectionError)
    }

    @Test
    fun testIfErrorIsUnexpectedErrorWhenThereIsAnIoexception() {
        // GIVEN
        val response = IllegalStateException()

        // WHEN
        val error = ErrorType.createFrom(response)

        // THEN
        assertTrue(error is ErrorType.UnexpectedException)
    }

    @Test
    fun testIfErrorIsHttpErrorBadRequestExceptionWhenThereIsAnException() {
        // GIVEN
        val response = responseExceptionOf(response = httpResponseOf(status = 400))

        // WHEN
        val error = ErrorType.createFrom(response)

        // THEN
        assertTrue(error is ErrorType.HttpError.BadRequestException)
    }

    @Test
    fun testIfErrorIsHttpErrorUnauthorizedExceptionWhenThereIsAnException() {
        // GIVEN
        val response = responseExceptionOf(response = httpResponseOf(status = 401))

        // WHEN
        val error = ErrorType.createFrom(response)

        // THEN
        assertTrue(error is ErrorType.HttpError.UnauthorizedException)
    }

    @Test
    fun testIfErrorIsHttpErrorForbiddenExceptionWhenThereIsAnException() {
        // GIVEN
        val response = responseExceptionOf(response = httpResponseOf(status = 403))

        // WHEN
        val error = ErrorType.createFrom(response)

        // THEN
        assertTrue(error is ErrorType.HttpError.ForbiddenException)
    }

    @Test
    fun testIfErrorIsHttpErrorNotFoundExceptionWhenThereIsAnException() {
        // GIVEN
        val response = responseExceptionOf(response = httpResponseOf(status = 404))

        // WHEN
        val error = ErrorType.createFrom(response)

        // THEN
        assertTrue(error is ErrorType.HttpError.NotFoundException)
    }

    @Test
    fun testIfErrorIsHttpErrorInternalServerErrorExceptionWhenThereIsStatusCodeFrom500To599() {
        // GIVEN
        val response = responseExceptionOf(response = httpResponseOf(status = (500..599).random()))

        // WHEN
        val error = ErrorType.createFrom(response)

        // THEN
        assertTrue(error is ErrorType.HttpError.InternalServerErrorException)
    }

    @Test
    fun testIfErrorTypeConnectionIsGettingMappedToLibraryErrorNetwork() {
        // GIVEN
        val error = ErrorType.ConnectionError(IOException(""))

        // WHEN
        val libraryError = error.toLibraryError()

        // THEN
        assertEquals(libraryError, LibraryError.Network)
    }

    @Test
    fun testIfErrorTypeUnauthorizedExceptionIsGettingMappedToLibraryWrongApplicationKey() {
        // GIVEN
        val error = ErrorType.HttpError.UnauthorizedException

        // WHEN
        val libraryError = error.toLibraryError()

        // THEN
        assertEquals(libraryError, LibraryError.WrongApplicationKey)
    }

    @Test
    fun testIfErrorTypeBadRequestExceptionIsGettingMappedToLibraryErrorServer() {
        // GIVEN
        val error = ErrorType.HttpError.BadRequestException

        // WHEN
        val libraryError = error.toLibraryError()

        // THEN
        assertEquals(libraryError, LibraryError.Server)
    }

    @Test
    fun testIfErrorTypeForbiddenExceptionIsGettingMappedToLibraryErrorServer() {
        // GIVEN
        val error = ErrorType.HttpError.ForbiddenException

        // WHEN
        val libraryError = error.toLibraryError()

        // THEN
        assertEquals(libraryError, LibraryError.Server)
    }

    @Test
    fun testIfErrorTypeNotFoundExceptionIsGettingMappedToLibraryErrorNetwork() {
        // GIVEN
        val error = ErrorType.HttpError.NotFoundException

        // WHEN
        val libraryError = error.toLibraryError()

        // THEN
        assertEquals(libraryError, LibraryError.Server)
    }

    @Test
    fun testIfErrorTypeInternalServerErrorExceptionIsGettingMappedToLibraryErrorNetwork() {
        // GIVEN
        val error = ErrorType.HttpError.InternalServerErrorException

        // WHEN
        val libraryError = error.toLibraryError()

        // THEN
        assertEquals(libraryError, LibraryError.Server)
    }

    @Test
    fun testIfErrorTypeUnexpectedExceptionExceptionIsGettingMappedToLibraryErrorNetwork() {
        // GIVEN
        val error = ErrorType.UnexpectedException(IllegalStateException())

        // WHEN
        val libraryError = error.toLibraryError()

        // THEN
        assertEquals(libraryError, LibraryError.Unexpected)
    }
}

internal fun responseExceptionOf(
    response: HttpResponse,
    cachedResponseText: String = "",
): ResponseException = ResponseException(
    response = response,
    cachedResponseText = cachedResponseText
)

class MockedHttpResponse(private val statusCode: HttpStatusCode) : HttpResponse() {
    override val call: HttpClientCall
        get() = throw IllegalStateException()

    override val content: ByteReadChannel
        get() = throw IllegalStateException()

    override val coroutineContext: CoroutineContext
        get() = throw IllegalStateException()

    override val headers: Headers
        get() = throw IllegalStateException()

    override val requestTime: GMTDate
        get() = throw IllegalStateException()

    override val responseTime: GMTDate
        get() = throw IllegalStateException()

    override val status: HttpStatusCode
        get() = statusCode

    override val version: HttpProtocolVersion
        get() = throw IllegalStateException()

    override fun toString(): String =
        "HttpResponse[$status]"
}

fun httpResponseOf(status: Int): HttpResponse =
    httpResponseOf(
        status = HttpStatusCode.fromValue(status)
    )

fun httpResponseOf(status: HttpStatusCode): HttpResponse = MockedHttpResponse(status)
