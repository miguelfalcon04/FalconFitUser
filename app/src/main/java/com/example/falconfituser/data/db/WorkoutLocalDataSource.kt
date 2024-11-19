package com.example.falconfituser.data.db

import com.example.falconfituser.data.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutLocalDataSource {
    suspend fun insert(workout: Workout)
    suspend fun update(workout: Workout)
    suspend fun delete(workout: Workout)
    suspend fun readAll():List<Workout>
    fun observeALl():Flow<List<Workout>>
}