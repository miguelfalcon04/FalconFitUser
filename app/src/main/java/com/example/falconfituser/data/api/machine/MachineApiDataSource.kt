package com.example.falconfituser.data.api.machine

import com.example.falconfituser.data.machine.Machine
import com.example.falconfituser.data.api.IFalconFitApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MachineApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): IMachineApiDataSource {
    override suspend fun readAll(): Response<MachineListRaw> {
        return ffApi.getMachines()
    }

    override suspend fun readOne(id: Int): Response<Machine> {
        return ffApi.getOneMachine(id)
    }
}