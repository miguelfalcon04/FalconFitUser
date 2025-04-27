package com.example.falconfituser.data.exercise

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Exercise (
    val id: String? = null,
    val title: String? = null,
    val subtitle: String? = null,
    val description: String? = null,
    val photo: String? = null
): Serializable