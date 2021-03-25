package com.audioburst.library.models

class UserPreferences(
    val id: String,
    val userId: String,
    val location: String?,
    val sourceType: String,
    val preferences: List<Preference>,
) {

    fun updatePreference(preferences: List<Preference>): UserPreferences =
        UserPreferences(
            id = id,
            userId = userId,
            location = location,
            sourceType = sourceType,
            preferences = preferences,
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as UserPreferences

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (location != other.location) return false
        if (sourceType != other.sourceType) return false
        if (preferences != other.preferences) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + sourceType.hashCode()
        result = 31 * result + preferences.hashCode()
        return result
    }

    override fun toString(): String {
        return "UserPreferences(id='$id', userId='$userId', location=$location, sourceType='$sourceType', preferences=$preferences)"
    }
}

class Preference(
    val name: String,
    val source: String,
    val take: Int,
    val offer: Int,
    val keys: List<Key>,
    val iconUrl: String?,
) {

    fun updateKeys(keys: List<Key>): Preference =
        Preference(
            name = name,
            source = source,
            take = take,
            offer = offer,
            keys = keys,
            iconUrl = iconUrl,
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Preference

        if (name != other.name) return false
        if (source != other.source) return false
        if (take != other.take) return false
        if (offer != other.offer) return false
        if (keys != other.keys) return false
        if (iconUrl != other.iconUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + take
        result = 31 * result + offer
        result = 31 * result + keys.hashCode()
        result = 31 * result + (iconUrl?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Preference(name='$name', source='$source', take=$take, offer=$offer, keys=$keys, iconUrl=$iconUrl)"
    }
}

class Key(
    val key: String,
    val segCategory: String,
    val source: String,
    val sourceId: Int,
    val position: Int,
    val selected: Boolean,
) {

    constructor(key: String, selected: Boolean = true) : this(
        key = key,
        segCategory = "NA",
        source = "trending",
        sourceId = 0,
        position = 1,
        selected = selected,
    )

    fun updateSelected(selected: Boolean): Key =
        Key(
            key = key,
            segCategory = segCategory,
            source = source,
            sourceId = sourceId,
            position = position,
            selected = selected,
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Key

        if (key != other.key) return false
        if (segCategory != other.segCategory) return false
        if (source != other.source) return false
        if (sourceId != other.sourceId) return false
        if (position != other.position) return false
        if (selected != other.selected) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + segCategory.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + sourceId
        result = 31 * result + position
        result = 31 * result + selected.hashCode()
        return result
    }

    override fun toString(): String {
        return "Key(key='$key', segCategory='$segCategory', source='$source', sourceId=$sourceId, position=$position, selected=$selected)"
    }
}
