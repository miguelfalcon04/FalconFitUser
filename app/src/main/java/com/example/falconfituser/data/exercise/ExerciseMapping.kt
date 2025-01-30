package com.example.falconfituser.data.exercise

import com.example.falconfituser.data.api.exercise.ExerciseRaw

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