package com.example.falconfituser.data.exercise

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IExerciseRepository {
    val setStream: StateFlow<List<Exercise>>
    suspend fun readAll(): List<Exercise>
    suspend fun readOne(id: Int): Exercise
    fun observeAll(): Flow<List<Exercise>>
}