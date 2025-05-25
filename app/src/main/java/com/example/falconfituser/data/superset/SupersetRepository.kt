package com.example.falconfituser.data.superset

import android.util.Log
import com.example.falconfituser.authentication.AuthenticationService
import com.example.falconfituser.data.Constants.Companion.BACKEND
import com.example.falconfituser.data.Constants.Companion.SUPERSETFB
import com.example.falconfituser.data.api.superset.ISupersetApiDataSource
import com.example.falconfituser.data.local.LocalRepository
import com.example.falconfituser.di.FirestoreSigleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Repository class responsible for managing Superset data.
 * Supports both Strapi and Firebase backends, with local caching fallback.
 */
class SupersetRepository @Inject constructor(
    private val apiData: ISupersetApiDataSource,
    private val localRepository: LocalRepository,
    private val authenticationService: AuthenticationService
): ISupersetRepository{
    private val _state = MutableStateFlow<List<Superset>>(listOf())
    override val setStream: StateFlow<List<Superset>>
        get() = _state.asStateFlow()

    val userId = authenticationService.getId()
    private val firestore = FirestoreSigleton.getInstance()
    private val supersetCollection = firestore.collection(SUPERSETFB)

    /**
     * Reads all Supersets for a specific user, depending on the backend.
     * Caches data locally for offline access in Strapi.
     *
     * @param id ID of the user
     * @return List of Supersets for the user
     */
    override suspend fun readAll(id: Int): List<Superset> {
        if( BACKEND === "strapi" ){
            try {
                val res = apiData.readAll(id)

                if(res.isSuccessful){
                    val supersList  = res.body()!!.data.toExternal()
                    _state.value = supersList

                    for (superset in supersList){
                        localRepository.createSuperset(superset.toLocal(id))
                    }

                    return supersList
                }
            }catch (e: Exception){
                val localSupersets = localRepository.getSupersetsByUser(id)
                    .first()
                    .map { it.toExternal() }

                _state.value = localSupersets
                return localSupersets
            }
        } else if ( BACKEND === "firebase" ){
            supersetCollection.whereEqualTo("userId", userId).get().addOnSuccessListener { querySnapshot ->
                val supersetList = mutableListOf<Superset>()
                for (document in querySnapshot.documents){
                    val superset = document.toObject(Superset::class.java)

                    // Lee la referencia del documento y la guarda localmente en cada Superset.
                    // Por eso al verlo en Firebase la variable document es null pero realmente la
                    // tomo aquí
                    val supersetWithDocId = superset!!.copy(document = document.id)

                    supersetWithDocId.let {
                        supersetList.add(it)
                    }

                }
                _state.value = supersetList.toList()
            }
        }
        

        return _state.value
    }

    /**
     * Reads a single Superset by ID.
     *
     * @param id Superset ID
     * @return Superset object
     */
    override suspend fun readOne(id: Int): Superset {
        TODO("Not yet implemented")
    }

    /**
     * Creates a new Superset entry in the selected backend.
     *
     * @param superset Superset to be created
     */
    override suspend fun createSuperset(superset: Superset) {
        if ( BACKEND === "strapi" ){
            val response = apiData.createSuperset(superset.toStrapi(userId.toInt()))

            if(response.isSuccessful){
                val id = response.body()!!.data.id
                localRepository.createSuperset(superset.toLocal(id))
            }
        } else if ( BACKEND === "firebase" ){
            supersetCollection.document().set(superset.copy(userId = userId))
        }
    }

    /**
     * Updates an existing Superset entry.
     *
     * @param supersetId Superset ID (Strapi)
     * @param superset Updated Superset data
     */
    override suspend fun updateSuperset(supersetId: Int, superset: Superset) {
        if ( BACKEND === "strapi" ) {
            apiData.updateSuperset(supersetId, superset.toStrapi(userId.toInt()))
        } else if ( BACKEND === "firebase" ) {
            val docRef = supersetCollection.document(superset.document!!)

            // Convierto el Superset a un Map para la actualización
            // Aqui si que guardo el document, por tenerlo mas a mano
            val supersetMap = superset.toMap()

            // Actualizo el documento
            docRef.update(supersetMap)
                .addOnSuccessListener {
                    Log.d("Firestore", "Documento actualizado con éxito")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al actualizar documento", e)
                }
        }
    }

    /**
     * Deletes a Superset from the backend and local repository if using Strapi.
     *
     * @param supersetId Superset ID (Strapi)
     * @param docReference Firebase document ID (Firebase)
     */
    override suspend fun deleteSuperset(supersetId: Int, docReference: String) {
        if ( BACKEND === "strapi" ) {
            localRepository.deleteSuperset(supersetId)
            apiData.deleteSuperset(supersetId)
        } else if ( BACKEND === "firebase" ) {
            supersetCollection.document(docReference).delete()
        }
    }

    /**
     * Observes all Supersets as a reactive flow.
     *
     * @return Flow of Superset list
     */
    override fun observeAll(): Flow<List<Superset>> {
        TODO("Not yet implemented")
    }
}