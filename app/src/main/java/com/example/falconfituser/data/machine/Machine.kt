package com.example.falconfituser.data.machine

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Machine(
    val id: String? = null,
    val title: String? = null,
    val subtitle: String? = null,
    val description: String? = null,
    val picture: String? = null,
    val taken: Boolean? = null
): Serializable