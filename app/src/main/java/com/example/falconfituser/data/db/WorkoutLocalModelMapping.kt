package com.example.falconfituser.data.db

import com.example.falconfituser.data.repository.Workout

fun Workout.toLocal():WorkoutEntity{
    return WorkoutEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        machineIds = this.machineIds
    )
}

// Cada elemento de tipo w (Workout) lo convierto a local
fun List<Workout>.toLocal(): List<WorkoutEntity>{
    return this.map{ w-> w.toLocal() }
}

fun WorkoutEntity.toExternal(): Workout{
    return Workout(
        id = this.id,
        name = this.name,
        description = this.description,
        machineIds = this.machineIds
    )
}

fun List<WorkoutEntity>.toExternal(): List<Workout>{
    return this.map{ w-> w.toExternal() }
}