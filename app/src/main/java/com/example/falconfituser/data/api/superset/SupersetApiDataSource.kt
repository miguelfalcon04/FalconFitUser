package com.example.falconfituser.data.api.superset

import com.example.falconfituser.data.api.IFalconFitApi
import retrofit2.Response
import javax.inject.Inject

class SupersetApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): ISupersetApiDataSource  {
    override suspend fun readAll(): Response<SupersetListRaw> {
        return ffApi.getSupersets()
    }

    override suspend fun readOne(id: Int): Response<SupersetListRaw> {
        return ffApi.getOneSuperset(id)
    }

    override suspend fun createSuperset(superset: SupersetPost) {
        return ffApi.createSuperset(superset)
    }

    override suspend fun updateSuperset(supersetId: Int, superset: SupersetPost) {
        return ffApi.updateSuperset(supersetId, superset)
    }

    override suspend fun deleteSuperset(supersetId: Int) {
        return ffApi.deleteSuperset(supersetId)
    }
}