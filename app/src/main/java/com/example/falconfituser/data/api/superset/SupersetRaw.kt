package com.example.falconfituser.data.api.superset

import com.example.falconfituser.data.api.exercise.ExerciseRaw
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

class UserIdRaw(
    val id: Int
)

data class ExerciseCollection(
    val data: List<ExerciseRaw> // lista de ejercicios dentro de `data`
)

data class SupersetPost(
    val data: SupersetRawPost
)

data class SupersetRawPost(
    val title: String,
    val exercises: List<ExercisePost>,
    val userId: UserIdRaw
)

data class ExercisePost(
    val id: Int
)