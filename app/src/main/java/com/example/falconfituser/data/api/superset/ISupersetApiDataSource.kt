package com.example.falconfituser.data.api.superset

import retrofit2.Response

interface ISupersetApiDataSource {
    suspend fun readAll(id: Int): Response<SupersetListRaw>
    suspend fun readOne(id:Int): Response<SupersetListRaw>
    suspend fun createSuperset(superset: SupersetPost)
    suspend fun updateSuperset(supersetId: Int, superset: SupersetPost)
    suspend fun deleteSuperset(supersetId: Int)
}