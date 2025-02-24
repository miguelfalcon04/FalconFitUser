package com.example.falconfituser.data.superset

import com.example.falconfituser.data.api.exercise.StrapiResponse
import com.example.falconfituser.data.api.superset.SupersetListRaw
import com.example.falconfituser.data.api.superset.SupersetPost
import com.example.falconfituser.data.api.superset.SupersetRaw
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response

interface ISupersetRepository {
    val setStream: StateFlow<List<Superset>>
    suspend fun readAll(id: Int): List<Superset>
    suspend fun readOne(id: Int): Superset
    suspend fun createSuperset(superset: SupersetPost): Response<StrapiResponse<SupersetRaw>>
    suspend fun updateSuperset(supersetId: Int, superset: SupersetPost): Response<SupersetListRaw>
    suspend fun deleteSuperset(supersetId: Int): Response<SupersetRaw>
    fun observeAll(): Flow<List<Superset>>
}