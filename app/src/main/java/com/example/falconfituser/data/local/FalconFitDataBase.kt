package com.example.falconfituser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.falconfituser.data.local.dao.ExerciseDao
import com.example.falconfituser.data.local.dao.MachineDao
import com.example.falconfituser.data.local.dao.PlacesDao
import com.example.falconfituser.data.local.dao.SupersetDao
import com.example.falconfituser.data.local.entities.ExerciseEntity
import com.example.falconfituser.data.local.entities.MachineEntity
import com.example.falconfituser.data.local.entities.PlacesEntity
import com.example.falconfituser.data.local.entities.SupersetEntity

/**
 * FalconFit local Room database definition.
 * Includes entities and abstract DAO access methods.
 * Version 3 is currently used.
 */
@Database(entities = [MachineEntity::class,
                      ExerciseEntity::class,
                      SupersetEntity::class,
                      PlacesEntity::class],
                      version = 3)
abstract class FalconFitDataBase(): RoomDatabase() {

    abstract fun machineDao(): MachineDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun supersetDao(): SupersetDao
    abstract fun placestDao(): PlacesDao

    companion object{
        @Volatile
        private var _INSTANCE: FalconFitDataBase? = null

        /**
         * Provides the singleton instance of the database.
         * Ensures only one instance is active at a time.
         * @param context Application context
         * @return FalconFitDataBase instance
         */
        fun getInstance(context: Context):FalconFitDataBase {
            return _INSTANCE ?: synchronized(this) {
                _INSTANCE ?: buildDatabase(context).also {
                        database -> _INSTANCE = database }
            }
        }

        /**
         * Builds the Room database with fallback to destructive migration.
         * @param context Application context
         * @return FalconFitDataBase instance
         */
        private fun buildDatabase(context:Context):FalconFitDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                FalconFitDataBase::class.java,
                "falconfit_db"
            )
            .fallbackToDestructiveMigration()
            .build()
        }

    }
}