package com.example.falconfituser.data.places

import com.example.falconfituser.data.api.places.IPlaceApiDataSource
import com.example.falconfituser.data.local.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val apiData: IPlaceApiDataSource,
    private val localRepository: LocalRepository
): IPlacesRepository {

    private val _state = MutableStateFlow<List<Places>>(listOf())
    override val setStream: StateFlow<List<Places>>
        get() = _state.asStateFlow()

    override suspend fun readAll(): List<Places> {
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

        return _state.value
    }

    override fun observeAll(): Flow<List<Places>> {
        TODO("Not yet implemented")
    }
}