package com.example.falconfituser.data.api

import com.example.falconfituser.data.api.FalconFitApi.FalconFitService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FalconFitApiRepository @Inject constructor(
    private val service: FalconFitService
){
    suspend fun getAll(): List<MachinesListApiModel>{
        val list = service.api.readAllMachines()
        val machinesApiModel = list.map {
            it.asApiModel()
        }
        return machinesApiModel
    }
}