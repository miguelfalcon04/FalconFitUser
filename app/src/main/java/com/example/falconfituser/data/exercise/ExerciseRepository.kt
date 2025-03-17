package com.example.falconfituser.data.exercise

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.falconfituser.authentication.AuthenticationService
import com.example.falconfituser.data.api.exercise.ExerciseRaw
import com.example.falconfituser.data.api.exercise.IExerciseApiDataSource
import com.example.falconfituser.data.api.exercise.StrapiResponse
import com.example.falconfituser.data.firebase.exercise.ExerciseFirebase
import com.example.falconfituser.data.local.LocalRepository
import com.example.falconfituser.di.ApiModule
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiData: IExerciseApiDataSource,
    private val fb: ExerciseFirebase,
    private val localRepository: LocalRepository,
    private val authenticationService: AuthenticationService
): IExerciseRepository {

    private val _state = MutableStateFlow<List<Exercise>>(listOf())
    override val setStream: StateFlow<List<Exercise>>
        get() = _state.asStateFlow()

    val userId = authenticationService.getId()

    // Debemos pasarle el id del usuario
    override suspend fun readAll(id: Int): List<Exercise> {
        try {
            // val res = apiData.readAll(id)
            val res = fb.getAllExercises(id)

            if(res.isNotEmpty()){

            }

            //if(res.isSuccessful){
            //    val execList = res.body()!!.data.toExternal()
            //    _state.value = execList

            //   for (exercise in execList) {
            //        localRepository.createExercise(exercise.toLocal(id))
            //    }

            //    return execList
            //}
        }catch (e: Exception){
            // Como devuelve un Flow tengo que mapear uno por uno los ejercicios
            val localExercises = localRepository.getExercisesByUser(id)
                .first() // Cojo el primer valor del Flow
                .map { it.toExternal() } // Mapeo de ExerciseEntity a Exercise

            _state.value = localExercises
            return localExercises
        }

        return _state.value
    }

    override suspend fun readOne(id: Int): Exercise {
        val res = apiData.readOne(id)
        return if(res.isSuccessful)res.body()!!
        else Exercise("0","fuera","no","furula", null)
    }

    override suspend fun createExercise(exercise: Exercise,
                                        photo: Uri?): Response<StrapiResponse<ExerciseRaw>> {
        val response = apiData.createExercise(exercise.toStrapi(userId))
        if(response.isSuccessful){
            val id = response.body()!!.data.id

            // Crea un documento, y añade los campos del ejercicio
            fb.createExercise(exercise)

            localRepository.createExercise(exercise.toLocal(id))
            photo?.let { uri ->
                uploadExercisePhoto(uri, id)
            }
        }
        return response
    }

    override suspend fun updateExercise(exerciseId: Int, exercise: Exercise, photo: Uri?) {
        val response = apiData.updateExercise(exerciseId, exercise.toStrapi(userId))
        if(response.isSuccessful){
            var uploadedExercise = response.body()

            photo?.let { uri ->
                uploadExercisePhoto(uri, uploadedExercise!!.data.id)
            }
        }
    }

    override suspend fun deleteExercise(exerciseId: Int): Response<StrapiResponse<ExerciseRaw>> {
        localRepository.deleteExercise(exerciseId)
        return apiData.deleteExercise(exerciseId)
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
            partMap["ref"] = "api::exercise.exercise".toRequestBody("text/plain".toMediaType())
            // Id del incidente
            partMap["refId"] = exerciseId.toString().toRequestBody("text/plain".toMediaType())
            // Campo de la colección
            partMap["field"] = "photo".toRequestBody("text/plain".toMediaType())

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