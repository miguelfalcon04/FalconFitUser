package com.example.falconfituser.data.superset

import com.example.falconfituser.data.api.superset.SupersetRaw

fun SupersetRaw.toExternal(): Superset{
    return Superset(
        id = this.id.toString(),
        title = this.attributes.title,
        exercises = emptyList()
        // this.attributes.exercises.data
    )
}

fun List<SupersetRaw>.toExternal():List<Superset> = map ( SupersetRaw::toExternal )