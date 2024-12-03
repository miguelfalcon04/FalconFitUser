package com.example.falconfituser.di

import com.example.falconfituser.data.api.exercise.ExerciseApiDataSource
import com.example.falconfituser.data.api.exercise.IExerciseApiDataSource
import com.example.falconfituser.data.exercise.ExerciseRepository
import com.example.falconfituser.data.exercise.IExerciseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExerciseRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindExerciseRepository(repository: ExerciseRepository): IExerciseRepository

    @Singleton
    @Binds
    abstract fun bindExerciseApi(api: ExerciseApiDataSource): IExerciseApiDataSource
}