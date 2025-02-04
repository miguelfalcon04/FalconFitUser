package com.example.falconfituser.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlacesEntity (
    @PrimaryKey val id: String,
    val title: String,
    val latitud: Float,
    val longitud: Float,
)