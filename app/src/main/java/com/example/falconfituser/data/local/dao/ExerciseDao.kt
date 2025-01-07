package com.example.falconfituser.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.falconfituser.data.local.entities.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createExercise(listExerciseEntity: List<ExerciseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createExercise(exerciseEntity: ExerciseEntity)

    @Upsert
    suspend fun updateExercise(exerciseEntity: ExerciseEntity)

    @Delete
    suspend fun deleteExercise(exerciseEntity: ExerciseEntity)

    @Query("SELECT * FROM exercise")
    fun getAllExercise(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercise WHERE userId = :id")
    fun getExerciseByUser(id: Int): Flow<List<ExerciseEntity>>
}