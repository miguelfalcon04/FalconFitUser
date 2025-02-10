package com.example.falconfituser.data.exercise

import android.net.Uri
import com.example.falconfituser.data.api.exercise.ExerciseCreateData
import com.example.falconfituser.data.api.exercise.ExerciseRaw
import com.example.falconfituser.data.api.exercise.StrapiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

interface IExerciseRepository {
    val setStream: StateFlow<List<Exercise>>
    suspend fun readAll(id: Int): List<Exercise>
    suspend fun readOne(id: Int): Exercise
    suspend fun createExercise(exercise: ExerciseCreateData,
                               photo: Uri?): Response<StrapiResponse<ExerciseRaw>>

    suspend fun updateExercise(exerciseId: Int, exercise: ExerciseCreateData, photo: Uri?)
    suspend fun deleteExercise(exerciseId: Int)
    fun observeAll(): Flow<List<Exercise>>
    suspend fun uploadExercisePhoto(uri: Uri, exerciseId: Int): Result<Uri>
}