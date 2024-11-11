package com.example.falconfituser.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Si el entrenamiento ya existe lo reemplaza
    suspend fun create(workout: WorkoutEntity)

    @Update
    suspend fun update(workout: WorkoutEntity)

    @Delete
    suspend fun delete(workout: WorkoutEntity)

    @Query("SELECT * FROM workout")
    suspend fun readAll(): List<WorkoutEntity>

    @Query("SELECT * FROM workout")
    fun observeAll(): Flow<List<WorkoutEntity>> // Actualiza en tiempo real cuando hay cambios
}