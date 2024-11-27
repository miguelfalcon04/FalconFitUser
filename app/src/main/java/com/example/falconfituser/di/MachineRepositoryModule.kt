package com.example.falconfituser.di

import com.example.falconfituser.data.api.machine.IMachineApiDataSource
import com.example.falconfituser.data.api.machine.MachineApiDataSource
import com.example.falconfituser.data.machine.IMachineRepository
import com.example.falconfituser.data.machine.MachineRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Singleton

@Module
@InstallIn(Singleton::class)
abstract class MachineRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindMachineRepository(repository: MachineRepository): IMachineRepository

    @Singleton
    @Binds
    abstract fun bindMachineApi(api: MachineApiDataSource): IMachineApiDataSource
}