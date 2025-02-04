package com.example.falconfituser.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.falconfituser.data.local.entities.SupersetEntity
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

    @Query("SELECT * FROM superset WHERE userId = :id")
    fun getSupersetByUser(id: Int): Flow<List<SupersetEntity>>
}