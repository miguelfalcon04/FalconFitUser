package com.example.falconfituser.data.api.places

import com.example.falconfituser.data.api.IFalconFitApi
import retrofit2.Response
import javax.inject.Inject

/**
 * Data source class that handles places API calls.
 * Implements the repository pattern by delegating API calls to Retrofit interface.
 */
class PlaceApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): IPlaceApiDataSource {

    /**
     * Fetches all places.
     * @return Response containing list of places
     */
    override suspend fun readAll(): Response<PlaceListRaw> {
        return ffApi.getPlaces()
    }
}