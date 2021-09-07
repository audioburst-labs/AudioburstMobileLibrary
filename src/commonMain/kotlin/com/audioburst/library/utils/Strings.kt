package com.audioburst.library.utils

internal object Strings {
    const val ERROR_NETWORK = "Internet connection error while communicating with the Audioburst API"

    const val ERROR_SERVER = "Server side error while communicating with the Audioburst API"

    const val ERROR_UNEXPECTED = "Something unexpected happened"

    const val ERROR_WRONG_APPLICATION_KEY = "Wrong Application Key has been used while communicating with the Audioburst API"

    const val ERROR_AD_URL_NOT_FOUND = "This Burst doesn't have Advertisement URL. Please call Burst.isAdAvailable to check whether Burst have advertisement or not"

    const val ERROR_NO_KEYS_SELECTED = "To be able to request personal playlist, you need to select at least one topic before."

    const val ERROR_NO_SEARCH_RESULTS = "No Bursts found for the query."

    const val NAME_PLACEHOLDER = "<<NAME>>"

    const val URL_PLACEHOLDER = "<<URL>>"

    const val DEFAULT_SHARE_BURST_TEXT = "Listen to: $NAME_PLACEHOLDER\n$URL_PLACEHOLDER"

    const val DEFAULT_SHARE_PLAYLIST_TEXT = "Listen to: $NAME_PLACEHOLDER playlist\n$URL_PLACEHOLDER"

    fun shareBurstMessage(title: String): String = "Listen to $title"

    fun sharePlaylistMessage(title: String): String = "Listen to $title playlist"
}
