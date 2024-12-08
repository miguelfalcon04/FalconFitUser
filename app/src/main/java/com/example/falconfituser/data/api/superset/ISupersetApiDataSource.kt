package com.example.falconfituser.data.api.superset

import retrofit2.Response

interface ISupersetApiDataSource {
    suspend fun readAll(): Response<SupersetListRaw>
    suspend fun readOne(id:Int): Response<SupersetListRaw>
    suspend fun createSuperset(superset: SupersetRaw)
    suspend fun updateSuperset(supersetId: Int, superset: SupersetRaw)
    suspend fun deleteSuperset(supersetId: Int)
}