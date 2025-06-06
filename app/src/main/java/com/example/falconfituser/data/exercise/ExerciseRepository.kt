package com.example.falconfituser.data.exercise

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.falconfituser.authentication.AuthenticationService
import com.example.falconfituser.data.Constants.Companion.BACKEND
import com.example.falconfituser.data.Constants.Companion.EXERCISEFB
import com.example.falconfituser.data.api.exercise.IExerciseApiDataSource
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

/**
 * Repository class that manages exercise data from multiple sources (API, local DB, Firebase).
 * Implements the repository pattern to abstract data access from the UI layer.
 */
class ExerciseRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiData: IExerciseApiDataSource,
    private val localRepository: LocalRepository,
    private val authenticationService: AuthenticationService
): IExerciseRepository {

    private val _state = MutableStateFlow<List<Exercise>>(listOf())
    override val setStream: StateFlow<List<Exercise>>
        get() = _state.asStateFlow()

    val userId = authenticationService.getId()
    private val firestore = FirestoreSigleton.getInstance()
    private val exercisesCollection = firestore.collection(EXERCISEFB)


    /**
     * Fetches all exercises for a user from either Strapi API or Firebase.
     * Falls back to local storage if API call fails.
     * @param userId User ID to fetch exercises for
     * @return List of exercises
     */
    override suspend fun readAll(userId: String): List<Exercise> {

        if(BACKEND === "strapi"){
            try {
                val res = apiData.readAll(userId.toInt())

                if(res.isSuccessful){
                    val execList = res.body()!!.data.toExternal()
                    _state.value = execList

                    for (exercise in execList) {
                        localRepository.createExercise(exercise.toLocal(userId.toInt()))
                    }

                    return execList
                }
            }catch (e: Exception){
                // Como devuelve un Flow tengo que mapear uno por uno los ejercicios
                val localExercises = localRepository.getExercisesByUser(userId.toInt())
                    .first() // Cojo el primer valor del Flow
                    .map { it.toExternal() } // Mapeo de ExerciseEntity a Exercise

                _state.value = localExercises
                return localExercises
            }
        }else if(BACKEND === "firebase"){
            exercisesCollection.whereEqualTo("userId", userId).get().addOnSuccessListener { querySnapshot ->
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

    /**
     * Fetches a single exercise by ID from API.
     * @param id Exercise ID to retrieve
     * @return Exercise object or default exercise if API fails
     */
    override suspend fun readOne(id: Int): Exercise {
        val res = apiData.readOne(id)
        return if(res.isSuccessful)res.body()!!
        else Exercise("0","fuera","no","furula", null)
    }

    /**
     * Creates a new exercise in the selected backend (Strapi or Firebase).
     * Handles photo upload if provided.
     * @param exercise Exercise data to create
     * @param photo Optional photo URI to upload
     */
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
            exercisesCollection.document().set(exerciseWithPhoto.copy(userId = authenticationService.getId()))
        }

    }

    /**
     * Updates an existing exercise in the selected backend.
     * @param exerciseId ID of exercise to update
     * @param exercise Updated exercise data
     * @param photo Optional new photo to upload
     */
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
            val docRef = exercisesCollection.document(exercise.document!!)
            val exerciseWithPhoto = uploadPhotoAndPostFirebase(exercise, photo)

            // Convierto el Exercise a un Map para la actualización
            // Aqui si que guardo el document, por tenerlo mas a mano
            val exerciseMap = exerciseWithPhoto.toMap()

            // Actualizo el documento
            docRef.update(exerciseMap)
                .addOnSuccessListener {
                    Log.d("Firestore", "Documento actualizado con éxito")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al actualizar documento", e)
                }
        }
    }

    /**
     * Deletes an exercise from the selected backend.
     * @param exerciseId Exercise ID (for Strapi)
     * @param docReference Document reference (for Firebase)
     */
    override suspend fun deleteExercise(exerciseId: Int, docReference: String) {
        if (BACKEND === "strapi"){
            localRepository.deleteExercise(exerciseId)
            apiData.deleteExercise(exerciseId)
        } else if ( BACKEND === "firebase"){
            exercisesCollection.document(docReference).delete()
        }
    }

    /**
     * Uploads exercise photo to Strapi backend using multipart form data.
     * @param uri Photo URI to upload
     * @param exerciseId Exercise ID to associate photo with
     * @return Result with uploaded photo URI or error
     */
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

    /**
     * Uploads photo to Firebase Storage and returns exercise with photo URL.
     * @param exercise Exercise object to add photo to
     * @param photo Photo URI to upload (nullable)
     * @return Exercise with photo URL or original exercise if no photo
     */
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