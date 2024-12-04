package com.example.falconfituser.data.api.exercise

import com.example.falconfituser.data.exercise.Exercise
import retrofit2.Response

interface IExerciseApiDataSource {
    suspend fun readAll(): Response<ExerciseListRaw>
    suspend fun readOne(id:Int): Response<Exercise>
    suspend fun createExercise(exercise: ExerciseRaw)
}