package com.example.falconfituser.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WorkoutEntity::class],
    version = 1
)
abstract class WorkoutDatabase:RoomDatabase() {
    abstract fun workoutDao():WorkoutDao
}