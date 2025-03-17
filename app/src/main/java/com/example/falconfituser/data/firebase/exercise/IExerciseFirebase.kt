package com.example.falconfituser.data.firebase.exercise

import com.example.falconfituser.data.exercise.Exercise

interface IExerciseFirebase {
    suspend fun createExercise(exercise: Exercise)
}