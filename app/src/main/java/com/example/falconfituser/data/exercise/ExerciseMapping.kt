package com.example.falconfituser.data.exercise

import com.example.falconfituser.data.api.exercise.ExerciseRaw
import com.example.falconfituser.data.machine.Machine
import com.example.falconfituser.data.api.machine.MachineRaw

fun ExerciseRaw.toExternal(): Exercise {
    return Exercise(
        id = this.id.toString(),
        title = this.attributes.title,
        subtitle = this.attributes.subtitle,
    )
}

fun List<ExerciseRaw>.toExternal():List<Exercise> = map(ExerciseRaw::toExternal)