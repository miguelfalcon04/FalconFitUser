package com.example.falconfituser.data.api.exercise

import com.example.falconfituser.data.api.IFalconFitApi
import com.example.falconfituser.data.exercise.Exercise
import retrofit2.Response
import javax.inject.Inject

class ExerciseApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): IExerciseApiDataSource {
    override suspend fun readAll(): Response<ExerciseListRaw> {
        return ffApi.getExercises()
    }

    override suspend fun readOne(id: Int): Response<Exercise> {
        return ffApi.getOneExercise(id)
    }
}