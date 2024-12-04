package com.example.falconfituser.data.exercise

import com.example.falconfituser.data.api.exercise.ExerciseCreateData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IExerciseRepository {
    val setStream: StateFlow<List<Exercise>>
    suspend fun readAll(): List<Exercise>
    suspend fun readOne(id: Int): Exercise
    suspend fun createExercise(exercise: ExerciseCreateData)
    fun observeAll(): Flow<List<Exercise>>
}