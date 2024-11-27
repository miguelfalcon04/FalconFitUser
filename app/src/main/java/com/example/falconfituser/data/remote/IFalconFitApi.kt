package com.example.falconfituser.data.remote

import com.example.falconfituser.data.Machine.Machine
import com.example.falconfituser.data.remote.machine.MachineListRaw
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IFalconFitApi {
    @GET("machines")
    suspend fun getTeams(): Response<MachineListRaw>
    @GET("machines/{id}")
    suspend fun getOneTeam(@Path("id")id: Int): Response<Machine>
}