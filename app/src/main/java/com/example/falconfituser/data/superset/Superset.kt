package com.example.falconfituser.data.superset

import com.example.falconfituser.data.exercise.Exercise

data class Superset (
    val id: String,
    val title: String,
    val exerciseOne: Exercise?,
    val exercisTwo: Exercise?,
)