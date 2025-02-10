package com.example.falconfituser.data.exercise

import com.example.falconfituser.data.api.exercise.ExerciseCreateData
import com.example.falconfituser.data.api.exercise.ExerciseRaw
import com.example.falconfituser.data.local.entities.ExerciseEntity

fun ExerciseRaw.toExternal(): Exercise {
    return Exercise(
        id = this.id.toString(),
        title = this.attributes.title,
        subtitle = this.attributes.subtitle,
        description = this.attributes.description,
        photo = this.attributes.photo?.data?.firstOrNull()?.attributes?.formats?.small?.url ?: null
    )
}

fun ExerciseCreateData.toLocal(id: String): ExerciseEntity{
    return ExerciseEntity(
        id = id,
        title = this.data.title,
        subtitle = this.data.subtitle,
        description = this.data.subtitle,
        userId = this.data.userId.id
    )
}

fun List<ExerciseRaw>.toExternal():List<Exercise> = map(ExerciseRaw::toExternal)