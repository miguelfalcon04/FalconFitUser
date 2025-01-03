package com.example.falconfituser.data.api.exercise

import com.example.falconfituser.data.exercise.Exercise
import retrofit2.Response

interface IExerciseApiDataSource {
    suspend fun readAll(id: Int): Response<ExerciseListRaw>
    suspend fun readOne(id:Int): Response<Exercise>
    suspend fun createExercise(exercise: ExerciseCreateData)
    suspend fun updateExercise(exerciseId: Int, exercise: ExerciseCreateData)
    suspend fun deleteExercise(exerciseId: Int)
}