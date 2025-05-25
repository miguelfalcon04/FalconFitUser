package com.example.falconfituser.data.api.superset

import com.example.falconfituser.data.api.IFalconFitApi
import com.example.falconfituser.data.api.exercise.StrapiResponse
import retrofit2.Response
import javax.inject.Inject


/**
 * Data source class that handles all superset-related API calls.
 * Implements the repository pattern by delegating API calls to Retrofit interface.
 */
class SupersetApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): ISupersetApiDataSource {

    /**
     * Fetches all supersets for a specific user.
     * @param id User ID to get supersets for
     * @return Response containing list of supersets
     */
    override suspend fun readAll(id: Int): Response<SupersetListRaw> {
        return ffApi.getSupersets(id)
    }

    /**
     * Fetches a single superset by ID.
     * @param id superset ID to retrieve
     * @return Response containing the superset data
     */
    override suspend fun readOne(id: Int): Response<SupersetListRaw> {
        return ffApi.getOneSuperset(id)
    }

    /**
     * Creates a new superset.
     * @param superset Superset data to create
     * @return Response with created superset wrapped in Strapi format
     */
    override suspend fun createSuperset(superset: SupersetPost): Response<StrapiResponse<SupersetRaw>> {
        return ffApi.createSuperset(superset)
    }

    /**
     * Updates an existing superset.
     * @param supersetId ID of superset to update
     * @param superset Updated superset data
     * @return Response with updated superset wrapped in Strapi format
     */
    override suspend fun updateSuperset(supersetId: Int,
                                        superset: SupersetPost): Response<StrapiResponse<SupersetRaw>> {
        return ffApi.updateSuperset(supersetId, superset)
    }

    /**
     * Deletes a superset.
     * @param supersetId ID of superset to delete
     * @return Response with deleted superset data
     */
    override suspend fun deleteSuperset(supersetId: Int): Response<SupersetRaw> {
        return ffApi.deleteSuperset(supersetId)
    }
}