package com.example.falconfituser.data.local

import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.falconfituser.data.local.dao.ExerciseDao
import com.example.falconfituser.data.local.dao.MachineDao
import com.example.falconfituser.data.local.dao.SupersetDao
import com.example.falconfituser.data.local.entities.ExerciseEntity
import com.example.falconfituser.data.local.entities.MachineEntity
import com.example.falconfituser.data.local.entities.SupersetEntity
import com.example.falconfituser.data.superset.Superset
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


    // Ejercicios
    // Flow de ejercicios que se actualizará automáticamente
    var exercises: Flow<List<ExerciseEntity>> = exerciseDao.getExerciseByUser(userId)


    @WorkerThread
    suspend fun createExercise(exerciseEntity: ExerciseEntity) {
        Log.d(TAG, "Inserting single exercise...")
        exerciseDao.createExercise(exerciseEntity)
        Log.d(TAG, "Exercise inserted")
    }

    fun getExercisesByUser(userId: Int): Flow<List<ExerciseEntity>> {
        return exerciseDao.getExerciseByUser(userId)
    }

    // Máquinas
    var machines: Flow<List<MachineEntity>> = machineDao.getAllMachine()

    // Superseries
    // Usamos SupersetWithExercisesEntity para tener la información completa

    @WorkerThread
    suspend fun createSuperset(supersetEntity: SupersetEntity) {
        Log.d(TAG, "Inserting single superset...")
        supersetDao.createSuperset(supersetEntity)
        Log.d(TAG, "Superset inserted")
    }

    fun getSupersetsByUser(userId: Int): Flow<List<SupersetEntity>> {
        return supersetDao.getSupersetByUser(userId)
    }

    @WorkerThread
    suspend fun updateSuperset(supersetEntity: SupersetEntity) {
        Log.d(TAG, "Updating superset...")
        supersetDao.updateSuperset(supersetEntity)
        Log.d(TAG, "Superset updated")
    }

    @WorkerThread
    suspend fun deleteExercise(exerciseEntity: ExerciseEntity) {
        Log.d(TAG, "Deleting exercise...")
        exerciseDao.deleteExercise(exerciseEntity)
        Log.d(TAG, "Exercise deleted")
    }

    @WorkerThread
    suspend fun deleteSuperset(supersetEntity: SupersetEntity) {
        Log.d(TAG, "Deleting superset...")
        supersetDao.deleteSuperset(supersetEntity)
        Log.d(TAG, "Superset deleted")
    }
}