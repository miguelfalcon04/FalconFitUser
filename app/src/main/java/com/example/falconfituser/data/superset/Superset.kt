package com.example.falconfituser.data.superset

import com.example.falconfituser.data.exercise.Exercise
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Superset (
    val id: String? = null,
    val title: String? = null,
    val exerciseOne: Exercise? = null,
    val exercisTwo: Exercise? = null,
    val document: String? = null
): Serializable