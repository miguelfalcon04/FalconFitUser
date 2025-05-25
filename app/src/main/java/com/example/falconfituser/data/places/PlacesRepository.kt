package com.example.falconfituser.data.places

import com.example.falconfituser.data.Constants.Companion.BACKEND
import com.example.falconfituser.data.Constants.Companion.PLACESFB
import com.example.falconfituser.data.api.places.IPlaceApiDataSource
import com.example.falconfituser.data.local.LocalRepository
import com.example.falconfituser.di.FirestoreSigleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Repository class responsible for managing places data.
 * Retrieves data from remote API or Firebase, with fallback to local storage.
 */
class PlacesRepository @Inject constructor(
    private val apiData: IPlaceApiDataSource,
    private val localRepository: LocalRepository
): IPlacesRepository {

    private val _state = MutableStateFlow<List<Places>>(listOf())
    override val setStream: StateFlow<List<Places>>
        get() = _state.asStateFlow()

    val firestore = FirestoreSigleton.getInstance()

    /**
     * Reads all places data depending on the selected backend (Strapi or Firebase).
     * Falls back to local database if API request fails.
     * @return List of Places objects
     */
    override suspend fun readAll(): List<Places> {
        if ( BACKEND === "strapi" ){
            try {
                val res = apiData.readAll()

                if (res.isSuccessful){
                    val placesList = res.body()!!.data.toExternal()
                    _state.value = placesList

                    for (place in placesList){
                        localRepository.createPlace(place.toLocal())
                    }

                    return placesList
                }
            }catch (e: Exception){
                val localPlaces = localRepository.getPlaces()
                    .first()
                    .map { it.toExternal() }

                _state.value = localPlaces
            }
        } else if( BACKEND === "firebase" ){
            firestore.collection(PLACESFB).get().addOnSuccessListener { querySnapshot ->
                val placeList = mutableListOf<Places>()
                for (document in querySnapshot.documents){
                    val place = document.toObject(Places::class.java)

                    place?.let {
                        placeList.add(it)
                    }

                }
                _state.value = placeList.toList()
            }
        }

        return _state.value
    }

    override fun observeAll(): Flow<List<Places>> {
        TODO("Not yet implemented")
    }
}