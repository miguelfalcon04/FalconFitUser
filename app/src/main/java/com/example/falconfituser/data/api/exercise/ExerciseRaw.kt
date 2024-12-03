package com.example.falconfituser.data.api.exercise

class ExerciseRaw (
    val id: Int,
    val attributes: ExerciseRawAttributes
)

data class ExerciseRawAttributes(
    val title: String,
    val subtitle: String,
    val description: String
)