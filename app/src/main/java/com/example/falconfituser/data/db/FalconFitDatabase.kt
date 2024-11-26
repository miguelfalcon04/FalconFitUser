package com.example.falconfituser.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.falconfituser.data.db.machine.MachineDao
import com.example.falconfituser.data.db.machine.MachineEntity
import com.example.falconfituser.data.db.workout.WorkoutDao
import com.example.falconfituser.data.db.workout.WorkoutEntity

@Database(
    entities = [MachineEntity::class],
    version = 1
)
abstract class FalconFitDatabase:RoomDatabase() {
    abstract fun machineDao(): MachineDao
}