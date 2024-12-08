package com.example.falconfituser.data.api.superset

import com.example.falconfituser.data.exercise.Exercise

// Al hacer un GET Bruno me devuelve
data class SupersetRaw(
    val id: Int, // El id
    val attributes: SupersetRawAttributes, // Los atributos (titulo y ejercicios)
)

data class SupersetRawAttributes(
    val title: String,
    val exercises: ExerciseCollection
)

data class ExerciseCollection(
    val data: List<Exercise>
)
