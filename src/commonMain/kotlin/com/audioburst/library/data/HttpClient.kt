package com.audioburst.library.data

import com.audioburst.library.data.remote.Endpoint
import com.audioburst.library.data.remote.toHttpRequest
import com.audioburst.library.models.Url
import com.audioburst.library.utils.Logger
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

internal suspend inline fun <reified T> HttpClient.execute(endpoint: Endpoint): Resource<T> =
    wrapIntoResource { request(endpoint.toHttpRequest()) }

internal suspend inline fun <reified T> HttpClient.execute(url: Url): Resource<T> =
    wrapIntoResource { request(URLBuilder(url.value).build()) }

internal inline fun <reified T> wrapIntoResource(executor: () -> T): Resource<T> =
    try {
        Resource.Data(executor())
    } catch (exception: Exception) {
        Logger.e("Exception: ${exception.message}")
        Resource.Error(ErrorType.createFrom(exception))
    }
