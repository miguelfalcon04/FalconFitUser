package com.example.falconfituser.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.falconfituser.data.local.entities.SupersetEntity
import com.example.falconfituser.data.local.entities.SupersetWithExercisesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupersetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createSuperset(listSupersetEntity: List<SupersetEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createSuperset(supersetEntity: SupersetEntity)

    @Upsert
    suspend fun updateSuperset(supersetEntity: SupersetEntity)

    @Delete
    suspend fun deleteSuperset(supersetEntity: SupersetEntity)

    @Query("""
        SELECT 
            s.id AS id,
            s.title AS title,
            s.exerciseOneId AS exerciseOneId,
            s.exerciseTwoId AS exerciseTwoId,
            s.userId AS userId,
            e1.title AS exerciseOneTitle,
            e1.subtitle AS exerciseOneSubtitle,
            e1.description AS exerciseOneDescription,
            e2.title AS exerciseTwoTitle,
            e2.subtitle AS exerciseTwoSubtitle,
            e2.description AS exerciseTwoDescription
        FROM superset s
        INNER JOIN exercise e1 ON e1.id = s.exerciseOneId
        INNER JOIN exercise e2 ON e2.id = s.exerciseTwoId
        WHERE s.userId = :userId
    """)
    fun getSupersetsByUser(userId: Int): Flow<List<SupersetWithExercisesEntity>>

    @Query("""
        SELECT 
            s.id AS id,
            s.title AS title,
            s.exerciseOneId AS exerciseOneId,
            s.exerciseTwoId AS exerciseTwoId,
            s.userId AS userId,
            e1.title AS exerciseOneTitle,
            e1.subtitle AS exerciseOneSubtitle,
            e1.description AS exerciseOneDescription,
            e2.title AS exerciseTwoTitle,
            e2.subtitle AS exerciseTwoSubtitle,
            e2.description AS exerciseTwoDescription
        FROM superset s
        INNER JOIN exercise e1 ON e1.id = s.exerciseOneId
        INNER JOIN exercise e2 ON e2.id = s.exerciseTwoId
    """)
    fun getAllSupersets(): Flow<List<SupersetWithExercisesEntity>>
}