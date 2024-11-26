package com.example.falconfituser.data.api

import com.example.falconfituser.data.Machine
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

interface FalconFitApi {
    @GET("api/machines")
    suspend fun readAllMachines(): List<MachineDetailResponse>

    @GET("api/machines/{id}")
    suspend fun readOneMachine(@Path("id") id: Int): Machine

    @Singleton
    class FalconFitService @Inject constructor(){
        private val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:1337/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api: FalconFitApi = retrofit.create(FalconFitApi::class.java)
    }


}