package com.example.falconfituser.data.loginRegister

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User(
    val uuid: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val role: String? = "user",
    val email: String? = null,
    val phoneNumber: String? = "",
    val registerDate: String? = "",
    val picture: String? = null
): Serializable