package com.example.falconfituser.data.api.places


class PlaceRaw (
    val id: Int,
    val attributes: PlaceRawAttributes
)

data class PlaceListRaw(
    val data: List<PlaceRaw>
)

data class PlaceRawAttributes(
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
)