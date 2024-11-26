package com.example.falconfituser.data.api

import com.example.falconfituser.data.Machine
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FalconFitApi {
    @GET("api/machines")
    suspend fun readAllMachines(): List<Machine>

    @GET("api/machines/{id}")
    suspend fun readOneMachine(@Path("id") id: Int): Machine


}