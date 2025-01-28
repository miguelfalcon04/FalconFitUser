package com.example.falconfituser.data.exercise

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.falconfituser.data.api.exercise.ExerciseCreateData
import com.example.falconfituser.data.api.exercise.IExerciseApiDataSource
import com.example.falconfituser.di.ApiModule
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiData: IExerciseApiDataSource,
): IExerciseRepository {

    private val _state = MutableStateFlow<List<Exercise>>(listOf())
    override val setStream: StateFlow<List<Exercise>>
        get() = _state.asStateFlow()

    // Debemos pasarle el id del usuario
    override suspend fun readAll(id: Int): List<Exercise> {
        val res = apiData.readAll(id)
        val exerc = _state.value.toMutableList()
        if(res.isSuccessful){
            val exercList = res.body()?.data?:emptyList()
            _state.value = exercList.toExternal()
        }
        else _state.value = exerc
        return exerc
    }

    override suspend fun readOne(id: Int): Exercise {
        val res = apiData.readOne(id)
        return if(res.isSuccessful)res.body()!!
        else Exercise("0","fuera","no","furula", null)
    }

    override suspend fun createExercise(exercise: ExerciseCreateData): Result<Exercise> {
        val response = apiData.createExercise(exercise)

        if(response.isSuccessful){
            var uploadedExercise

            exercise.data.photo?.let { uri ->
                val imageUploaded = uploadExercisePhoto(uri,response.body()!!.data.id)
                // Si ha subido obtenemos la Uri
                if( imageUploaded.isSuccess) {
                    val uploadedUri = imageUploaded.getOrNull()!!
                    uploadedExercise = uploadedExercise.copy(
                        photoUri = uploadedUri
                    )
                }
            }


        }
    }

    override suspend fun updateExercise(exerciseId: Int, exercise: ExerciseCreateData) {
        apiData.updateExercise(exerciseId, exercise)
    }

    override suspend fun deleteExercise(exerciseId: Int) {
        apiData.deleteExercise(exerciseId)
    }

    override suspend fun uploadExercisePhoto(
        uri: Uri,
        exerciseId: Int
    ): Result<Uri> {
        try{
            // Obtenemos el resolver de MediaStore
            val resolver = context.contentResolver
            // Abrimos el input stream a partir de la URI
            val inputStream = resolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Cannot open InputStream from Uri")
            // Obtenemos el tipo del fichero
            val mimeType = resolver.getType(uri) ?: "image/*"
            // Obtenemos el nombre local, esto podiamos cambiarlo a otro patrón
            val fileName = uri.lastPathSegment ?: "evidence.jpg"
            // Convertimos el fichero a cuerpo de la petición
            val requestBody = inputStream.readBytes().toRequestBody(mimeType.toMediaTypeOrNull())


            // Construimos la parte de la petición
            val part = MultipartBody.Part.createFormData("files", fileName, requestBody)
            // Map con el resto de parámetros
            val partMap: MutableMap<String, RequestBody> = mutableMapOf()

            // Referencia
            partMap["ref"] = "api::incident.incident".toRequestBody("text/plain".toMediaType())
            // Id del incidente

            partMap["refId"] = exerciseId.toString().toRequestBody("text/plain".toMediaType())
            // Campo de la colección
            partMap["field"] = "evidence".toRequestBody("text/plain".toMediaType())

            // Subimos el fichero
            val imageResponse = apiData.addExercisePhoto(
                partMap,
                files = part,
            )
            // Si ha ido mal la subida, salimos con error
            if (!imageResponse.isSuccessful) {
                return Result.failure(Exception("Error al subir imagen"))
            }
            else {
                val remoteUri= "${ApiModule.STRAPI}${imageResponse.body()!!.first().formats.small.url}"
                return Result.success(remoteUri.toUri())
            }

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override fun observeAll(): Flow<List<Exercise>> {
        TODO("Not yet implemented")
    }
}