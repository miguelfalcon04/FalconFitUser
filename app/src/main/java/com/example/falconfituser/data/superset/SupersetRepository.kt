package com.example.falconfituser.data.superset

import com.example.falconfituser.data.api.superset.ISupersetApiDataSource
import com.example.falconfituser.data.api.superset.SupersetRaw
import com.example.falconfituser.data.exercise.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class SupersetRepository @Inject constructor(
    private val apiData: ISupersetApiDataSource,
): ISupersetRepository{
    private val _state = MutableStateFlow<List<Superset>>(listOf())
    override val setStream: StateFlow<List<Superset>>
        get() = _state.asStateFlow()

    override suspend fun readAll(): List<Superset> {
        val res = apiData.readAll()
        val supers = _state.value.toMutableList()
        if(res.isSuccessful){
            val supersetList = res.body()?.data?:emptyList()
            _state.value = supersetList.toExternal()
        }else{
            _state.value = supers
        }
        return supers
    }

    override suspend fun readOne(id: Int): Superset {
        TODO("Not yet implemented")
//        val res = apiData.readOne(id)
//        return if (res.isSuccessful) {
//            res.body().toExternal() // Mapea de SupersetRaw a Superset
//        } else {
//            Superset(
//                id = "0",
//                title = "Sin datos",
//                exercises = emptyList() // Lista vacía de ejercicios
//            )
//        }
    }


    override suspend fun createSuperset(superset: SupersetRaw) {
        return apiData.createSuperset(superset)
    }

    override suspend fun updateSuperset(supersetId: Int, superset: SupersetRaw) {
        return apiData.updateSuperset(supersetId, superset)
    }

    override suspend fun deleteSuperset(supersetId: Int) {
        return apiData.deleteSuperset(supersetId)
    }

    override fun observeAll(): Flow<List<Superset>> {
        TODO("Not yet implemented")
    }
}