package com.example.falconfituser.data.exercise

import com.example.falconfituser.data.api.exercise.ExerciseCreateData
import com.example.falconfituser.data.api.exercise.ExerciseRaw
import com.example.falconfituser.data.local.entities.ExerciseEntity
import kotlinx.coroutines.flow.Flow

fun ExerciseRaw.toExternal(): Exercise {
    return Exercise(
        id = this.id.toString(),
        title = this.attributes.title,
        subtitle = this.attributes.subtitle,
        description = this.attributes.description,
        photo = this.attributes.photo?.data?.firstOrNull()?.attributes?.formats?.small?.url ?: null
    )
}

fun List<ExerciseRaw>.toExternal():List<Exercise> = map(ExerciseRaw::toExternal)

fun ExerciseCreateData.toLocal(id: String): ExerciseEntity{
    return ExerciseEntity(
        id = id,
        title = this.data.title,
        subtitle = this.data.subtitle,
        description = this.data.description,
        userId = this.data.userId.id
    )
}

fun ExerciseEntity.toExternal(): Exercise {
    return Exercise(
        id = this.id,
        title = this.title,
        subtitle = this.subtitle,
        description = this.description,
        photo = null  // O puedes asignar una URL por defecto si tienes almacenado ese dato
    )
}

fun Exercise.toLocal(userId: Int): ExerciseEntity{
    return ExerciseEntity(
        id = this.id,
        title = this.title,
        subtitle = this.subtitle,
        description = this.description,
        userId = userId
    )
}

fun List<ExerciseEntity>.toExternalEntities(): List<Exercise> = map { it.toExternal() }

