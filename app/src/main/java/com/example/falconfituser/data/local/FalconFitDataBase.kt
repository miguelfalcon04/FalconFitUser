package com.example.falconfituser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.falconfituser.data.local.dao.ExerciseDao
import com.example.falconfituser.data.local.dao.MachineDao
import com.example.falconfituser.data.local.dao.SupersetDao
import com.example.falconfituser.data.local.entities.ExerciseEntity
import com.example.falconfituser.data.local.entities.MachineEntity
import com.example.falconfituser.data.local.entities.SupersetEntity

@Database(entities = [MachineEntity::class,
                      ExerciseEntity::class,
                      SupersetEntity::class],
                      version = 1)
abstract class FalconFitDataBase(): RoomDatabase() {

    abstract fun machineDao(): MachineDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun supersetDao(): SupersetDao

    companion object{
        @Volatile
        private var _INSTANCE: FalconFitDataBase? = null

        fun getInstance(context: Context):FalconFitDataBase {
            return _INSTANCE ?: synchronized(this) {
                _INSTANCE ?: buildDatabase(context).also {
                        database -> _INSTANCE = database }
            }
        }

        private fun buildDatabase(context:Context):FalconFitDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                FalconFitDataBase::class.java,
                "falconfit_db"
            ).build()
        }

    }
}