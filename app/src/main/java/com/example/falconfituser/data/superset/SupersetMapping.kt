package com.example.falconfituser.data.superset

import com.example.falconfituser.data.api.superset.SupersetRaw
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.exercise.toExternal

fun SupersetRaw.toExternal(): Superset{
    // En caso de que se borre un ejercicio asociado a un superset para que no de petardazo
    val exercises = this.attributes.exercises.data
    val exerciseOne = exercises.get(0)?.toExternal()?: Exercise("0", "Ejercicio no disponible", "Ninguno", "Nada")
    val exerciseTwo = exercises.get(1)?.toExternal()?: Exercise("0", "Ejercicio no disponible", "Ninguno", "Nada")
    return Superset(
        id = this.id.toString(),
        title = this.attributes.title,
        exerciseOne = exerciseOne,
        exercisTwo = exerciseTwo
    )
}

fun List<SupersetRaw>.toExternal():List<Superset> = map ( SupersetRaw::toExternal )