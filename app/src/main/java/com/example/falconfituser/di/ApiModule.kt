package com.example.falconfituser.di

import com.example.falconfituser.authentication.AuthenticationInterceptor
import com.example.falconfituser.authentication.AuthenticationService
import com.example.falconfituser.data.api.IFalconFitApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    companion object {
        const val STRAPI = "https://falconfitrender.onrender.com/api/"
    }

    /**
     * Anotación para calificar el interceptor de autenticación
     */
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AuthInterceptorOkHttpClient

    @Provides
    @AuthInterceptorOkHttpClient
    fun provideAutenticationInterceptor(authentication: AuthenticationService): Interceptor {
        return AuthenticationInterceptor(authentication)
    }

    @Provides
    @Singleton
    fun provideIFalconFitApi(client: OkHttpClient): IFalconFitApi{
        return Retrofit.Builder()
            .baseUrl(STRAPI)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IFalconFitApi::class.java)
    }
}