package com.example.falconfituser.di

import com.example.falconfituser.data.api.superset.ISupersetApiDataSource
import com.example.falconfituser.data.api.superset.SupersetApiDataSource
import com.example.falconfituser.data.superset.ISupersetRepository
import com.example.falconfituser.data.superset.SupersetRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SupersetRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindSupersetRepository(repository: SupersetRepository): ISupersetRepository

    @Singleton
    @Binds
    abstract fun bindSupersetApi(api: SupersetApiDataSource): ISupersetApiDataSource
}