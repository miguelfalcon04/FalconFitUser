package com.example.falconfituser.data.api

import com.example.falconfituser.data.machine.Machine
import com.example.falconfituser.data.api.machine.MachineListRaw
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IFalconFitApi {
    @GET("machines")
    suspend fun getMachines(): Response<MachineListRaw>
    @GET("machines/{id}")
    suspend fun getOneMachine(@Path("id")id: Int): Response<Machine>
}