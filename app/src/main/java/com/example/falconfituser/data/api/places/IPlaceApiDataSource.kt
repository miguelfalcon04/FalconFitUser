package com.example.falconfituser.data.api.places

import retrofit2.Response

interface IPlaceApiDataSource {
    suspend fun readAll(): Response<PlaceListRaw>
}