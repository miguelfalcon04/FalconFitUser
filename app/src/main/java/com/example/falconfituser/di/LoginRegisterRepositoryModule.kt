package com.example.falconfituser.di

import com.example.falconfituser.data.api.loginRegister.ILoginRegisterApiDataSource
import com.example.falconfituser.data.api.loginRegister.LoginRegisterApiDataSource
import com.example.falconfituser.data.loginRegister.ILoginRegisterRepository
import com.example.falconfituser.data.loginRegister.LoginRegisterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginRegisterRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindLoginRegisterRepository(repository: LoginRegisterRepository): ILoginRegisterRepository

    @Singleton
    @Binds
    abstract fun bindLoginRegisterApiDataSource(repository: LoginRegisterApiDataSource): ILoginRegisterApiDataSource
}