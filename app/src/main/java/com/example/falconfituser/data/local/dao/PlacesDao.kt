package com.example.falconfituser.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.falconfituser.data.local.entities.PlacesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {
    @Query("SELECT * FROM places")
    fun getPlaces(): Flow<List<PlacesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlace(placeEntity: PlacesEntity)
}