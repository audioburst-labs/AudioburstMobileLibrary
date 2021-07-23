package com.audioburst.library.utils

internal object Strings {
    const val errorNetwork = "Internet connection error while communicating with the Audioburst API"

    const val errorServer = "Server side error while communicating with the Audioburst API"

    const val errorUnexpected = "Something unexpected happened"

    const val errorWrongApplicationKey = "Wrong Application Key has been used while communicating with the Audioburst API"

    const val errorAdUrlNotFound = "This Burst doesn't have Advertisement URL. Please call Burst.isAdAvailable to check whether Burst have advertisement or not"

    const val errorNoKeysSelected = "To be able to request personal playlist, you need to select at least one topic before."

    const val errorNoSearchResults = "No Bursts found for the query."

    const val namePlaceholder = "<<NAME>>"

    const val urlPlaceholder = "<<URL>>"

    const val defaultShareBurstText = "Listen to: $namePlaceholder\n$urlPlaceholder"

    const val defaultSharePlaylistText = "Listen to: $namePlaceholder playlist\n$urlPlaceholder"

    fun shareBurstMessage(title: String): String = "Listen to $title"

    fun sharePlaylistMessage(title: String): String = "Listen to $title playlist"
}
