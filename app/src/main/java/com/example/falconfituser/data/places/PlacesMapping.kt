package com.example.falconfituser.data.places

import com.example.falconfituser.data.api.places.PlaceRaw
import com.example.falconfituser.data.local.entities.PlacesEntity

fun PlaceRaw.toExternal(): Places{
    return Places(
        id = this.id.toString(),
        title = this.attributes.title,
        latitude = this.attributes.latitud,
        longitude = this.attributes.longitud,
    )
}

fun Places.toLocal(): PlacesEntity{
    return PlacesEntity(
        id = this.id,
        title = this.title,
        latitud = this.latitude.toFloat(),
        longitud = this.longitude.toFloat()
    )
}

fun PlacesEntity.toExternal(): Places{
    return Places(
        id = this.id,
        title = this.title,
        latitude = this.latitud.toDouble(),
        longitude = this.latitud.toDouble(),
    )
}

fun List<PlaceRaw>.toExternal():List<Places> = map(PlaceRaw::toExternal)