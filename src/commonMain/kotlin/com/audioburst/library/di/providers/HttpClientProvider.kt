package com.audioburst.library.di.providers

import com.audioburst.library.utils.LibraryConfiguration
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.http.*

internal class HttpClientProvider(
    private val serializerProvider: Provider<JsonSerializer>,
    private val libraryConfigurationProvider: Provider<LibraryConfiguration>,
) : Singleton<HttpClient>() {

    override fun creator(): HttpClient =
        HttpClient {
            install(JsonFeature) {
                serializer = serializerProvider.get()
                accept(ContentType.Application.Json, ContentType.Application.OctetStream)
            }
            defaultRequest {
                val configuration = libraryConfigurationProvider.get()
                headers.append(SUBSCRIPTION_KEY_NAME, configuration.subscriptionKey.value)
                headers.append(VERSION_NUMBER_NAME, configuration.libraryVersion.value)
            }
        }

    companion object {
        private const val SUBSCRIPTION_KEY_NAME = "Ocp-Apim-Subscription-Key"
        private const val VERSION_NUMBER_NAME = "Ocp-Apim-Version-Number"
    }
}
