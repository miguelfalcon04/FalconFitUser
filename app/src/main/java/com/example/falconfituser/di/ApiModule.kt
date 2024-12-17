package com.example.falconfituser.di

import com.example.falconfituser.data.api.IFalconFitApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideIFalconFitApi(): IFalconFitApi{
        val ffApiUrl = "https://falconfitadmin2version.onrender.com/api/"
        return Retrofit.Builder()
            .baseUrl(ffApiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IFalconFitApi::class.java)
    }
}