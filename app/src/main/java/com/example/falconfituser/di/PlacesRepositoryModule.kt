package com.example.falconfituser.di

import com.example.falconfituser.data.api.places.IPlaceApiDataSource
import com.example.falconfituser.data.api.places.PlaceApiDataSource
import com.example.falconfituser.data.places.IPlacesRepository
import com.example.falconfituser.data.places.PlacesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlacesRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindPlacesRepository(repository: PlacesRepository): IPlacesRepository

    @Singleton
    @Binds
    abstract fun bindPlacesApi(api: PlaceApiDataSource): IPlaceApiDataSource
}