package com.example.falconfituser.data.local

import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.falconfituser.data.local.dao.ExerciseDao
import com.example.falconfituser.data.local.dao.MachineDao
import com.example.falconfituser.data.local.dao.SupersetDao
import com.example.falconfituser.data.local.entities.ExerciseEntity
import com.example.falconfituser.data.local.entities.SupersetEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val machineDao: MachineDao,
    private val supersetDao: SupersetDao,
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val TAG = "LocalRepository"
    }

    val userId = sharedPreferences.getString("USER_ID", null)?.toIntOrNull() ?: 0

    // Exercise
    fun getExercisesByUser(userId: Int): Flow<List<ExerciseEntity>> {
        return exerciseDao.getExerciseByUser(userId)
    }

    @WorkerThread
    suspend fun createExercise(exerciseEntity: ExerciseEntity) {
        exerciseDao.createExercise(exerciseEntity)
    }

    @WorkerThread
    suspend fun deleteExercise(exerciseEntity: ExerciseEntity) {
        exerciseDao.deleteExercise(exerciseEntity)
    }

    // Máquinas

    fun getSupersetsByUser(userId: Int): Flow<List<SupersetEntity>> {
        return supersetDao.getSupersetByUser(userId)
    }

    @WorkerThread
    suspend fun createSuperset(supersetEntity: SupersetEntity) {
        Log.d(TAG, "Inserting single superset...")
        supersetDao.createSuperset(supersetEntity)
        Log.d(TAG, "Superset inserted")
    }

    @WorkerThread
    suspend fun deleteSuperset(supersetEntity: SupersetEntity) {
        Log.d(TAG, "Deleting superset...")
        supersetDao.deleteSuperset(supersetEntity)
        Log.d(TAG, "Superset deleted")
    }
}