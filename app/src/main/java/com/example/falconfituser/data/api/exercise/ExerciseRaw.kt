package com.example.falconfituser.data.api.exercise

class ExerciseRaw (
    val id: Int,
    val attributes: ExerciseRawAttributes
)

class UserIdRaw(
    val id: Int
)

data class ExerciseRawAttributes(
    val title: String,
    val subtitle: String,
    val description: String,
    val userId: UserIdRaw
)

data class ExerciseCreateData(
    val data: ExerciseRawAttributes
)
