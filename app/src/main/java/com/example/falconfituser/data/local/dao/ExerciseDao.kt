package com.example.falconfituser.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.falconfituser.data.local.entities.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createExercise(exerciseEntity: ExerciseEntity)

    @Query("DELETE FROM exercise WHERE id = :exerciseId")
    suspend fun deleteExercise(exerciseId: Int)

    @Query("SELECT * FROM exercise WHERE userId = :id")
    fun getExerciseByUser(id: Int): Flow<List<ExerciseEntity>>
}