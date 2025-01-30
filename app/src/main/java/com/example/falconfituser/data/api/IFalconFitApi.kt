package com.example.falconfituser.data.api

import com.example.falconfituser.data.api.exercise.CreatedMediaItemResponse
import com.example.falconfituser.data.api.exercise.ExerciseCreateData
import com.example.falconfituser.data.api.exercise.ExerciseListRaw
import com.example.falconfituser.data.api.exercise.ExerciseRaw
import com.example.falconfituser.data.api.exercise.StrapiResponse
import com.example.falconfituser.data.api.loginRegister.LoginRaw
import com.example.falconfituser.data.api.loginRegister.LoginResponse
import com.example.falconfituser.data.api.loginRegister.RegisterRaw
import com.example.falconfituser.data.api.machine.MachineListRaw
import com.example.falconfituser.data.api.superset.SupersetListRaw
import com.example.falconfituser.data.api.superset.SupersetPost
import com.example.falconfituser.data.exercise.Exercise
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface IFalconFitApi {
    @GET("machines/?populate=photo")
    suspend fun getMachines(): Response<MachineListRaw>

    @GET("exercises/?populate=photo")
    suspend fun getExercises(@Query("filters[userId][id]") userId: Int): Response<ExerciseListRaw>
    @GET("exercises/{id}")
    suspend fun getOneExercise(@Path("id")id: Int): Response<Exercise>
    @POST("exercises") // @Body es que lo que ponga en exercise se va a enviar en el cuerpo de la solicitud
    suspend fun createExercise(@Body exercise: ExerciseCreateData): Response<StrapiResponse<ExerciseRaw>>
    @PUT("exercises/{id}") // Hace el update
    suspend fun updateExercise(@Path("id")id: Int, @Body exercise: ExerciseCreateData)
    @DELETE("exercises/{id}")
    suspend fun deleteExercise(@Path("id")id: Int)

    @GET("supersets/?populate=exercises")
    suspend fun getSupersets(@Query("filters[userId][id]") userId: Int): Response<SupersetListRaw>
    @GET("supersets/{id}")
    suspend fun getOneSuperset(@Path("id")id: Int): Response<SupersetListRaw>
    @POST("supersets")
    suspend fun createSuperset(@Body superset: SupersetPost)
    @PUT("supersets/{id}")
    suspend fun updateSuperset(@Path("id")id: Int, @Body superset: SupersetPost)
    @DELETE("supersets/{id}")
    suspend fun deleteSuperset(@Path("id")id: Int)

    @POST("auth/local/register")
    suspend fun register(@Body userToRegister: RegisterRaw): Response<LoginResponse>
    @POST("auth/local")
    suspend fun login(@Body userToLogin: LoginRaw): Response<LoginResponse>

    @Multipart
    @POST("/api/upload")
    suspend fun addExercisePhoto(@PartMap partMap: MutableMap<String, RequestBody>,
                                    @Part files: MultipartBody.Part ):Response<List<CreatedMediaItemResponse>>
}