package com.example.falconfituser.data.api.places

import com.example.falconfituser.data.api.IFalconFitApi
import com.example.falconfituser.data.api.machine.IMachineApiDataSource
import com.example.falconfituser.data.api.machine.MachineListRaw
import retrofit2.Response
import javax.inject.Inject

class PlaceApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): IPlaceApiDataSource {
    override suspend fun readAll(): Response<PlaceListRaw> {
        val res = ffApi.getPlaces()
        return res
    }
}