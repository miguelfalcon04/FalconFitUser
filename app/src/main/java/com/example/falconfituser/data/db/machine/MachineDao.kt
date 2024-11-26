package com.example.falconfituser.data.db.machine

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MachineDao {

    @Query("SELECT * FROM machine")
    fun getAll(): Flow<List<MachineEntity>>
}