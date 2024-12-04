package com.example.falconfituser.data.api

import com.example.falconfituser.data.api.exercise.ExerciseListRaw
import com.example.falconfituser.data.api.exercise.ExerciseRaw
import com.example.falconfituser.data.machine.Machine
import com.example.falconfituser.data.api.machine.MachineListRaw
import com.example.falconfituser.data.exercise.Exercise
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IFalconFitApi {
    @GET("machines")
    suspend fun getMachines(): Response<MachineListRaw>

    @GET("exercises")
    suspend fun getExercises(): Response<ExerciseListRaw>
    @GET("exercises/{id}")
    suspend fun getOneExercise(@Path("id")id: Int): Response<Exercise>
    @POST("exercises")
    suspend fun createExercise(exercise: ExerciseRaw)
}