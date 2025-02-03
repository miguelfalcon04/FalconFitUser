package com.example.falconfituser.data.places

import com.example.falconfituser.data.api.machine.IMachineApiDataSource
import com.example.falconfituser.data.api.places.IPlaceApiDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val apiData: IPlaceApiDataSource,
): IPlacesRepository {

    private val _state = MutableStateFlow<List<Places>>(listOf())
    override val setStream: StateFlow<List<Places>>
        get() = _state.asStateFlow()

    override suspend fun readAll(): List<Places> {
        val res = apiData.readAll()
        val place = _state.value.toMutableList()
        if(res.isSuccessful){
            val mchnList = res.body()?.data?:emptyList()
            _state.value = mchnList.toExternal()
        }
        else _state.value = place
        return place
    }

    override fun observeAll(): Flow<List<Places>> {
        TODO("Not yet implemented")
    }
}