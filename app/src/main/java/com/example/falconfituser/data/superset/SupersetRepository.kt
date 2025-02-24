package com.example.falconfituser.data.superset

import com.example.falconfituser.data.api.exercise.StrapiResponse
import com.example.falconfituser.data.api.superset.ISupersetApiDataSource
import com.example.falconfituser.data.api.superset.SupersetListRaw
import com.example.falconfituser.data.api.superset.SupersetPost
import com.example.falconfituser.data.api.superset.SupersetRaw
import com.example.falconfituser.data.local.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject

class SupersetRepository @Inject constructor(
    private val apiData: ISupersetApiDataSource,
    private val localRepository: LocalRepository
): ISupersetRepository{
    private val _state = MutableStateFlow<List<Superset>>(listOf())
    override val setStream: StateFlow<List<Superset>>
        get() = _state.asStateFlow()

    override suspend fun readAll(id: Int): List<Superset> {
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

        return _state.value
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


    override suspend fun createSuperset(superset: SupersetPost): Response<StrapiResponse<SupersetRaw>> {
        val response = apiData.createSuperset(superset)

        if(response.isSuccessful){
            val id = response.body()!!.data.id.toString()
            localRepository.createSuperset(superset.toLocal(id))
        }

        return response
    }

    override suspend fun updateSuperset(supersetId: Int, superset: SupersetPost): Response<SupersetListRaw> {
        return apiData.updateSuperset(supersetId, superset)
    }

    override suspend fun deleteSuperset(supersetId: Int): Response<SupersetRaw> {
        localRepository.deleteSuperset(supersetId)
        return apiData.deleteSuperset(supersetId)
    }

    override fun observeAll(): Flow<List<Superset>> {
        TODO("Not yet implemented")
    }
}