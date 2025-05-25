package com.example.falconfituser.data.api.exercise

import com.example.falconfituser.data.api.IFalconFitApi
import com.example.falconfituser.data.exercise.Exercise
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

/**
 * Data source class that handles all exercise-related API calls.
 * Implements the repository pattern by delegating API calls to Retrofit interface.
 */
class ExerciseApiDataSource @Inject constructor(
    private val ffApi: IFalconFitApi
): IExerciseApiDataSource {

    /**
     * Fetches all exercises for a specific user.
     * @param id User ID to get exercises for
     * @return Response containing list of exercises
     */
    override suspend fun readAll(id: Int): Response<ExerciseListRaw> {
        return ffApi.getExercises(id)
    }

    /**
     * Fetches a single exercise by ID.
     * @param id Exercise ID to retrieve
     * @return Response containing the exercise data
     */
    override suspend fun readOne(id: Int): Response<Exercise> {
        return ffApi.getOneExercise(id)
    }

    /**
     * Creates a new exercise.
     * @param exercise Exercise data to create
     * @return Response with created exercise wrapped in Strapi format
     */
    override suspend fun createExercise(exercise: ExerciseCreateData):
            Response<StrapiResponse<ExerciseRaw>> {
        return ffApi.createExercise(exercise)
    }

    /**
     * Updates an existing exercise.
     * @param exerciseId ID of exercise to update
     * @param exercise Updated exercise data
     * @return Response with updated exercise wrapped in Strapi format
     */
    override suspend fun updateExercise(exerciseId: Int,
                                        exercise: ExerciseCreateData):
            Response<StrapiResponse<ExerciseRaw>> {
        return ffApi.updateExercise(exerciseId, exercise)
    }

    /**
     * Deletes an exercise.
     * @param exerciseId ID of exercise to delete
     * @return Response with deleted exercise data
     */
    override suspend fun deleteExercise(exerciseId: Int): Response<StrapiResponse<ExerciseRaw>> {
        return ffApi.deleteExercise(exerciseId)
    }

    /**
     * Uploads a photo for an exercise using multipart form data.
     * @param partMap Form data fields as key-value pairs
     * @param files Image file to upload
     * @return Response containing uploaded media item information
     */
    override suspend fun addExercisePhoto(
        partMap: MutableMap<String, RequestBody>,
        files: MultipartBody.Part
    ): Response<List<CreatedMediaItemResponse>> {
        return ffApi.addExercisePhoto(partMap, files)
    }
}