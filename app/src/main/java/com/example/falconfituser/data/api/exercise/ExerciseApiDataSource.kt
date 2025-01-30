package com.example.falconfituser.data.api.exercise

import android.net.Uri
import com.example.falconfituser.data.api.IFalconFitApi
import com.example.falconfituser.data.exercise.Exercise
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ExerciseApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): IExerciseApiDataSource {
    override suspend fun readAll(id: Int): Response<ExerciseListRaw> {
        return ffApi.getExercises(id)
    }

    override suspend fun readOne(id: Int): Response<Exercise> {
        return ffApi.getOneExercise(id)
    }

    override suspend fun createExercise(exercise: ExerciseCreateData): Response<StrapiResponse<ExerciseRaw>> {
        return ffApi.createExercise(exercise)
    }

    override suspend fun updateExercise(exerciseId: Int,
                                        exercise: ExerciseCreateData): Response<StrapiResponse<ExerciseRaw>> {
        return ffApi.updateExercise(exerciseId, exercise)
    }

    override suspend fun deleteExercise(exerciseId: Int) {
        return ffApi.deleteExercise(exerciseId)
    }

    override suspend fun addExercisePhoto(
        partMap: MutableMap<String, RequestBody>,
        files: MultipartBody.Part
    ): Response<List<CreatedMediaItemResponse>> {
        return ffApi.addExercisePhoto(partMap, files)
    }

}