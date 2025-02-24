package com.example.falconfituser.data.api.superset

import com.example.falconfituser.data.api.IFalconFitApi
import com.example.falconfituser.data.api.exercise.StrapiResponse
import retrofit2.Response
import javax.inject.Inject

class SupersetApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): ISupersetApiDataSource  {
    override suspend fun readAll(id: Int): Response<SupersetListRaw> {
        return ffApi.getSupersets(id)
    }

    override suspend fun readOne(id: Int): Response<SupersetListRaw> {
        return ffApi.getOneSuperset(id)
    }

    override suspend fun createSuperset(superset: SupersetPost): Response<StrapiResponse<SupersetRaw>> {
        return ffApi.createSuperset(superset)
    }

    override suspend fun updateSuperset(supersetId: Int,
                                        superset: SupersetPost): Response<SupersetListRaw> {
        return ffApi.updateSuperset(supersetId, superset)
    }

    override suspend fun deleteSuperset(supersetId: Int): Response<SupersetRaw> {
        return ffApi.deleteSuperset(supersetId)
    }
}