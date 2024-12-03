package com.example.falconfituser.data.api

import com.example.falconfituser.data.api.exercise.ExerciseListRaw
import com.example.falconfituser.data.machine.Machine
import com.example.falconfituser.data.api.machine.MachineListRaw
import com.example.falconfituser.data.exercise.Exercise
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IFalconFitApi {
    @GET("machines")
    suspend fun getMachines(): Response<MachineListRaw>
    @GET("machines/{id}")
    suspend fun getOneMachine(@Path("id")id: Int): Response<Machine>

    @GET("exercises")
    suspend fun getExercises(): Response<ExerciseListRaw>
    @GET("exercises/{id}")
    suspend fun getOneExercise(@Path("id")id: Int): Response<Exercise>
}