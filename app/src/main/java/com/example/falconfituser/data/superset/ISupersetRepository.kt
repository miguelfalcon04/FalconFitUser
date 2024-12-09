package com.example.falconfituser.data.superset

import com.example.falconfituser.data.api.superset.SupersetPost
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ISupersetRepository {
    val setStream: StateFlow<List<Superset>>
    suspend fun readAll(): List<Superset>
    suspend fun readOne(id: Int): Superset
    suspend fun createSuperset(superset: SupersetPost)
    suspend fun updateSuperset(supersetId: Int, superset: SupersetPost)
    suspend fun deleteSuperset(supersetId: Int)
    fun observeAll(): Flow<List<Superset>>
}