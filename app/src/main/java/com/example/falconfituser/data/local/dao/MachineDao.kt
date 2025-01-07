package com.example.falconfituser.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.falconfituser.data.local.entities.MachineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MachineDao {
    @Query("SELECT * FROM machine")
    fun getAllMachine(): Flow<List<MachineEntity>>
}