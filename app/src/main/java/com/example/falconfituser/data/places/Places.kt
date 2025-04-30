package com.example.falconfituser.data.places

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Places (
    val id: String? = null,
    val title: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null
): Serializable