package com.example.falconfituser.data.api.machine

import com.example.falconfituser.data.api.IFalconFitApi
import retrofit2.Response
import javax.inject.Inject

/**
 * Data source class that handles machine API calls.
 * Implements the repository pattern by delegating API calls to Retrofit interface.
 */
class MachineApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): IMachineApiDataSource {

    /**
     * Fetches all machines.
     * @return Response containing list of machines
     */
    override suspend fun readAll(): Response<MachineListRaw> {
        return ffApi.getMachines()
    }
}