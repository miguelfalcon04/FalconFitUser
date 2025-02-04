package com.example.falconfituser.data.superset

import com.example.falconfituser.data.api.superset.SupersetRaw
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.exercise.toExternal

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

fun List<SupersetRaw>.toExternal():List<Superset> = map ( SupersetRaw::toExternal )