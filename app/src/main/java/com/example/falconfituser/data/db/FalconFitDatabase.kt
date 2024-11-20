package com.example.falconfituser.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.falconfituser.data.db.workout.WorkoutDao
import com.example.falconfituser.data.db.workout.WorkoutEntity

@Database(
    entities = [WorkoutEntity::class],
    version = 1
)
abstract class FalconFitDatabase:RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
}