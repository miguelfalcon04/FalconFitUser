package com.example.falconfituser.data.places

import com.example.falconfituser.data.api.places.PlaceRaw

fun PlaceRaw.toExternal(): Places{
    return Places(
        id = this.id.toString(),
        title = this.attributes.title,
        latitude = this.attributes.latitud,
        longitude = this.attributes.longitud,
    )
}

fun List<PlaceRaw>.toExternal():List<Places> = map(PlaceRaw::toExternal)