package com.audioburst.library.models

data class UserPreferences(
    val id: String,
    val userId: String,
    val location: String?,
    val sourceType: String,
    val preferences: List<Preference>,
)

data class Preference(
    val name: String,
    val source: String,
    val take: Int,
    val offer: Int,
    val keys: List<Key>,
)

data class Key(
    val key: String,
    val segCategory: String,
    val source: String,
    val sourceId: Int,
    val position: Int,
    val selected: Boolean,
)
