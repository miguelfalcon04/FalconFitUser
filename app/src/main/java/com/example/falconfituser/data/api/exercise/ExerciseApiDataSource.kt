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

    override suspend fun createExercise(exercise: ExerciseCreateData) {
        return ffApi.createExercise(exercise)
    }

    override suspend fun updateExercise(exerciseId: Int, exercise: ExerciseCreateData) {
        return ffApi.updateExercise(exerciseId, exercise)
    }

    override suspend fun deleteExercise(exerciseId: Int) {
        return ffApi.deleteExercise(exerciseId)
    }

}