package com.example.falconfituser.data.api.machine

import com.example.falconfituser.data.api.IFalconFitApi
import retrofit2.Response
import javax.inject.Inject

class MachineApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): IMachineApiDataSource {
    override suspend fun readAll(): Response<MachineListRaw> {
        return ffApi.getMachines()
    }
}