package com.example.falconfituser.data.api.superset

import retrofit2.Response

interface ISupersetApiDataSource {
    suspend fun readAll(id: Int): Response<SupersetListRaw>
    suspend fun readOne(id:Int): Response<SupersetListRaw>
    suspend fun createSuperset(superset: SupersetPost): Response<SupersetRaw>
    suspend fun updateSuperset(supersetId: Int, superset: SupersetPost): Response<SupersetListRaw>
    suspend fun deleteSuperset(supersetId: Int): Response<SupersetRaw>
}