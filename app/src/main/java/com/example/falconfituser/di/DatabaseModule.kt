package com.example.falconfituser.di

import android.content.Context
import com.example.falconfituser.data.local.FalconFitDataBase
import com.example.falconfituser.data.local.dao.ExerciseDao
import com.example.falconfituser.data.local.dao.MachineDao
import com.example.falconfituser.data.local.dao.SupersetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): FalconFitDataBase {
        return FalconFitDataBase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideMachineDao(database: FalconFitDataBase): MachineDao {
        return database.machineDao()
    }

    @Singleton
    @Provides
    fun provideExerciseDao(database: FalconFitDataBase): ExerciseDao {
        return database.exerciseDao()
    }

    @Singleton
    @Provides
    fun provideSupersetDao(database: FalconFitDataBase): SupersetDao {
        return database.supersetDao()
    }
}