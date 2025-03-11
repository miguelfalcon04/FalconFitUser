package com.example.falconfituser.data.superset

import com.example.falconfituser.data.api.superset.ExercisePost
import com.example.falconfituser.data.api.superset.SupersetPost
import com.example.falconfituser.data.api.superset.SupersetRaw
import com.example.falconfituser.data.api.superset.SupersetRawPost
import com.example.falconfituser.data.api.superset.UserIdRaw
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.exercise.toExternal
import com.example.falconfituser.data.local.entities.SupersetEntity

fun SupersetRaw.toExternal(): Superset{
    // En caso de que se borre un ejercicio asociado a un superset para que no de error
    val exercises = this.attributes.exercises.data
    val exerciseOne = exercises.getOrNull(0)?.toExternal()?: Exercise("0", "Ejercicio no disponible", "Ninguno", "Nada", null)
    val exerciseTwo = exercises.getOrNull(1)?.toExternal()?: Exercise("0", "Ejercicio no disponible", "Ninguno", "Nada", null)
    return Superset(
        id = this.id.toString(),
        title = this.attributes.title,
        exerciseOne = exerciseOne,
        exercisTwo = exerciseTwo
    )
}

fun Superset.toStrapi(userId: Int): SupersetPost{
    val exercisesList = mutableListOf<ExercisePost>()

    exerciseOne?.id?.toIntOrNull()?.let { exerciseId ->
        exercisesList.add(ExercisePost(id = exerciseId))
    }

    exercisTwo?.id?.toIntOrNull()?.let { exerciseId ->
        exercisesList.add(ExercisePost(id = exerciseId))
    }

    return SupersetPost(
        data = SupersetRawPost(
            title = this.title,
            exercises = exercisesList,
            userId = UserIdRaw(
                id = userId
            )
        )
    )
}

fun SupersetPost.toLocal(id: String): SupersetEntity{
    return SupersetEntity(
        id = id,
        title = this.data.title,
        exerciseOneTitle = this.data.exercises.toString(),
        exerciseTwoTitle = this.data.exercises.toString(),
        userId = this.data.userId.id
    )
}

fun Superset.toLocal(userId: Int): SupersetEntity{
    return SupersetEntity(
        id = this.id,
        title = this.title+" Sin Conexión",
        exerciseOneTitle = this.exerciseOne!!.title,
        exerciseTwoTitle = this.exercisTwo!!.title,
        userId = userId
    )
}

fun SupersetEntity.toExternal(): Superset{
    return Superset(
        id = this.id,
        title = this.title,
        exerciseOne = Exercise("0", exerciseOneTitle, "Ninguno", "Nada", null),
        exercisTwo = Exercise("0", exerciseTwoTitle, "Ninguno", "Nada", null)
    )
}

fun List<SupersetRaw>.toExternal():List<Superset> = map ( SupersetRaw::toExternal )