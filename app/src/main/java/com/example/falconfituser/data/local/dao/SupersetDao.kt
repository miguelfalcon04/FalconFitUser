package com.example.falconfituser.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.falconfituser.data.local.entities.SupersetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SupersetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createSuperset(supersetEntity: SupersetEntity)

    @Query("DELETE FROM superset WHERE id = :supersetId")
    suspend fun deleteSuperset(supersetId: Int)

    @Query("SELECT * FROM superset WHERE userId = :id")
    fun getSupersetByUser(id: Int): Flow<List<SupersetEntity>>
}