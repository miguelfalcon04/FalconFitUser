package com.example.falconfituser.data.api

import com.example.falconfituser.data.api.exercise.ExerciseCreateData
import com.example.falconfituser.data.api.exercise.ExerciseListRaw
import com.example.falconfituser.data.api.machine.MachineListRaw
import com.example.falconfituser.data.exercise.Exercise
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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

    @POST("exercises") // @Body es que lo que ponga en exercise se va a enviar en el cuerpo de la solicitud
    suspend fun createExercise(@Body exercise: ExerciseCreateData)
    @DELETE("exercises/{id}")
    suspend fun deleteExercise(@Path("id")id: Int)
}