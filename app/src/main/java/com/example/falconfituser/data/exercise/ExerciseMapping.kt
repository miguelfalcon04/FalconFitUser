package com.example.falconfituser.data.exercise

import com.example.falconfituser.data.api.exercise.ExerciseCreateData
import com.example.falconfituser.data.api.exercise.ExerciseRaw
import com.example.falconfituser.data.api.exercise.ExerciseRawAttributes
import com.example.falconfituser.data.api.exercise.UserIdRaw
import com.example.falconfituser.data.local.entities.ExerciseEntity

fun Exercise.toStrapi(userId: String): ExerciseCreateData{
    return ExerciseCreateData(
        data = ExerciseRawAttributes(
            title = this.title!!,
            subtitle = this.subtitle!!,
            description = this.description!!,
            userId = UserIdRaw(
                id = userId.toInt()
            )
        )
    )
}

fun ExerciseRaw.toExternal(): Exercise {
    return Exercise(
        id = this.id.toString(),
        title = this.attributes.title,
        subtitle = this.attributes.subtitle,
        description = this.attributes.description,
        photo = this.attributes.photo?.data?.firstOrNull()?.attributes?.formats?.small?.url
    )
}

fun List<ExerciseRaw>.toExternal():List<Exercise> = map(ExerciseRaw::toExternal)

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
        id = this.id!!,
        title = this.title!!,
        subtitle = this.subtitle!!,
        description = this.description!!,
        userId = userId
    )
}

fun Exercise.toMap(): Map<String, Any?> {
    return mapOf(
        "id" to id,
        "title" to title,
        "subtitle" to subtitle,
        "description" to description,
        "document" to document,
        "photo" to photo,
        "userId" to userId
    )
}
