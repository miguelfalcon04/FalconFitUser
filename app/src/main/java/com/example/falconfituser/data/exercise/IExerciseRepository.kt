package com.example.falconfituser.data.exercise

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IExerciseRepository {
    val setStream: StateFlow<List<Exercise>>
    suspend fun readAll(userId: String): List<Exercise>
    suspend fun readOne(id: Int): Exercise
    suspend fun createExercise(exercise: Exercise,
                               photo: Uri?)

    suspend fun updateExercise(exerciseId: Int, exercise: Exercise, photo: Uri?)
    suspend fun deleteExercise(exerciseId: Int, docReference: String)
    fun observeAll(): Flow<List<Exercise>>
    suspend fun uploadExercisePhoto(uri: Uri, exerciseId: Int): Result<Uri>
    suspend fun uploadPhotoAndPostFirebase(exercise: Exercise, photo: Uri?): Exercise
}