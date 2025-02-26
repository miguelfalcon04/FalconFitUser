package com.example.falconfituser.data.api.places

import com.example.falconfituser.data.api.IFalconFitApi
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