package com.example.falconfituser.data.remote.machine

import com.example.falconfituser.data.Machine.Machine
import com.example.falconfituser.data.remote.IFalconFitApi
import retrofit2.Response
import javax.inject.Inject

class MachineApiDataSource @Inject constructor(
    private val machineApi: IFalconFitApi
): IMachineApiDataSource {
    override suspend fun readAll(): Response<MachineListRaw> {
        TODO("Not yet implemented")
    }

    override suspend fun readOne(id: Int): Response<Machine> {
        TODO("Not yet implemented")
    }
}