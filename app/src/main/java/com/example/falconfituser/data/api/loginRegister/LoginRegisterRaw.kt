package com.example.falconfituser.data.api.loginRegister

data class RegisterRaw(
    val email: String,
    val username: String,
    val password: String
)

data class LoginRaw(
    val identifier: String,
    val password: String
)