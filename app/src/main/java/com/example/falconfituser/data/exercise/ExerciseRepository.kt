package com.example.falconfituser.data.exercise

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.falconfituser.authentication.AuthenticationService
import com.example.falconfituser.data.Constants.Companion.BACKEND
import com.example.falconfituser.data.Constants.Companion.EXERCISEFB
import com.example.falconfituser.data.api.exercise.ExerciseRaw
import com.example.falconfituser.data.api.exercise.IExerciseApiDataSource
import com.example.falconfituser.data.api.exercise.StrapiResponse
import com.example.falconfituser.data.firebase.exercise.ExerciseFirebase
import com.example.falconfituser.data.local.LocalRepository
import com.example.falconfituser.di.ApiModule
import com.example.falconfituser.di.FirestoreSigleton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

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
    private val firestore = FirestoreSigleton.getInstance()
    private val exercisesCollection = firestore.collection(EXERCISEFB)


    // Debemos pasarle el id del usuario
    override suspend fun readAll(id: Int): List<Exercise> {

        if(BACKEND === "strapi"){
            try {
                val res = apiData.readAll(id)

                if(res.isSuccessful){
                    val execList = res.body()!!.data.toExternal()
                    _state.value = execList

                    for (exercise in execList) {
                        localRepository.createExercise(exercise.toLocal(id))
                    }

                    return execList
                }
            }catch (e: Exception){
                // Como devuelve un Flow tengo que mapear uno por uno los ejercicios
                val localExercises = localRepository.getExercisesByUser(id)
                    .first() // Cojo el primer valor del Flow
                    .map { it.toExternal() } // Mapeo de ExerciseEntity a Exercise

                _state.value = localExercises
                return localExercises
            }
        }else if(BACKEND === "firebase"){
            exercisesCollection.get().addOnSuccessListener { querySnapshot ->
                val exerciseList = mutableListOf<Exercise>()
                for (document in querySnapshot.documents){
                    val exercise = document.toObject(Exercise::class.java)

                    // Lee la referencia del documento y la guarda localmente en cada Ejercicio.
                    // Por eso al verlo en Firebase la variable document es null pero realmente la
                    // tomo aquí
                    val exerciseWithDocId = exercise!!.copy(document = document.id)

                    exerciseWithDocId.let {
                        exerciseList.add(it)
                    }

                }
                _state.value = exerciseList.toList()
            }
        }

        return _state.value
    }

    override suspend fun readOne(id: Int): Exercise {
        val res = apiData.readOne(id)
        return if(res.isSuccessful)res.body()!!
        else Exercise("0","fuera","no","furula", null)
    }

    override suspend fun createExercise(exercise: Exercise,
                                        photo: Uri?) {
        if(BACKEND === "strapi"){
            val response = apiData.createExercise(exercise.toStrapi(userId))
            if(response.isSuccessful){
                val id = response.body()!!.data.id

                localRepository.createExercise(exercise.toLocal(id))
                photo?.let { uri ->
                    uploadExercisePhoto(uri, id)
                }
            }
        }else if(BACKEND === "firebase"){
            // Se sube el document vacio ya que no puedo asiganrle el valor antes de haberse creado
            // Por eso al hacer el readAll debo tomar el id del documento y guardarlo en el ejercicio
            val exerciseWithPhoto = uploadPhotoAndPostFirebase(exercise, photo)
            exercisesCollection.document().set(exerciseWithPhoto)
        }

    }

    override suspend fun updateExercise(exerciseId: Int, exercise: Exercise, photo: Uri?) {
        if ( BACKEND === "strapi" ){
            val response = apiData.updateExercise(exerciseId, exercise.toStrapi(userId))
            if(response.isSuccessful){
                var uploadedExercise = response.body()

                photo?.let { uri ->
                    uploadExercisePhoto(uri, uploadedExercise!!.data.id)
                }
            }
        } else if (BACKEND === "firebase" ) {
            if (exercise.document != null) {
                val docRef = exercisesCollection.document(exercise.document)

                // Convierto el Exercise a un Map para la actualización
                // Aqui si que guardo el document, por tenerlo mas a mano
                val exerciseMap = exercise.toMap()

                // Actualizo el documento
                docRef.update(exerciseMap)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Documento actualizado con éxito")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error al actualizar documento", e)
                    }
            } else {
                // Manejo del caso donde no hay ID de documento
                Log.e("Firestore", "No se puede actualizar: ID de documento nulo")

            }
        }
    }

    override suspend fun deleteExercise(exerciseId: Int, docReference: String) {
        if (BACKEND === "strapi"){
            localRepository.deleteExercise(exerciseId)
            apiData.deleteExercise(exerciseId)
        } else if ( BACKEND === "firebase"){
            exercisesCollection.document(docReference).delete()
        }
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

    override suspend fun uploadPhotoAndPostFirebase(
        exercise: Exercise,
        photo: Uri?
    ): Exercise {
        // Si no hay foto devuelvo el ejercicio sin modificar
        if (photo == null) return exercise

        return try {
            // Convertir Uri a ByteArray
            val bytes = context.contentResolver.openInputStream(photo)?.use { it.readBytes() }
                ?: return exercise

            // Obtener el tipo MIME
            val mimeType = context.contentResolver.getType(photo) ?: "image/jpeg"

            // Cre0 nombre de archivo
            val timestamp = System.currentTimeMillis()
            val random = (Math.random() * 1000000).toInt()
            val fileName = "uploads/${timestamp}_${random}"

            // Referencia a Firebase Storage
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child(fileName)

            // Crear metadata
            val metadata = StorageMetadata.Builder()
                .setContentType(mimeType)
                .setCustomMetadata("uploaded-by", userId ?: "anonymous")
                .build()

            // Subir el archivo y obtener la URL
            val downloadUrl = suspendCancellableCoroutine<Uri> { continuation ->
                val uploadTask = storageRef.putBytes(bytes, metadata)

                continuation.invokeOnCancellation {
                    if (uploadTask.isInProgress) {
                        uploadTask.cancel()
                    }
                }

                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    storageRef.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resumeWith(Result.success(task.result))
                    } else {
                        task.exception?.let { continuation.resumeWith(Result.failure(it)) }
                    }
                }
            }

            // Añadir al ejercicio la URL de la foto
            exercise.copy(photo = downloadUrl.toString())
        } catch (e: Exception) {
            // Registrar error y devolver el ejercicio sin cambios
            e.printStackTrace()
            exercise
        }
    }

    override fun observeAll(): Flow<List<Exercise>> {
        TODO("Not yet implemented")
    }
}