package com.example.falconfituser.data.local

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.falconfituser.data.local.dao.ExerciseDao
import com.example.falconfituser.data.local.dao.MachineDao
import com.example.falconfituser.data.local.dao.SupersetDao
import com.example.falconfituser.data.local.entities.ExerciseEntity
import com.example.falconfituser.data.local.entities.MachineEntity
import com.example.falconfituser.data.local.entities.SupersetEntity
import com.example.falconfituser.data.local.entities.SupersetWithExercisesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val machineDao: MachineDao,
    private val supersetDao: SupersetDao
) {
    companion object {
        private const val TAG = "LocalRepository"
    }

    // Ejercicios
    // Flow de ejercicios que se actualizará automáticamente
    var exercises: Flow<List<ExerciseEntity>> = exerciseDao.getAllExercise()

    @WorkerThread
    suspend fun insertExercises(listExerciseEntity: List<ExerciseEntity>) {
        Log.d(TAG, "Inserting exercises...")
        exerciseDao.createExercise(listExerciseEntity)
        exercises = exerciseDao.getAllExercise()
        Log.d(TAG, "Exercises inserted")
    }

    @WorkerThread
    suspend fun insertSingleExercise(exerciseEntity: ExerciseEntity) {
        Log.d(TAG, "Inserting single exercise...")
        exerciseDao.createExercise(exerciseEntity)
        exercises = exerciseDao.getAllExercise()
        Log.d(TAG, "Exercise inserted")
    }

    fun getExercisesByUser(userId: Int): Flow<List<ExerciseEntity>> {
        return exerciseDao.getExerciseByUser(userId)
    }

    // Máquinas
    var machines: Flow<List<MachineEntity>> = machineDao.getAllMachine()

    // Superseries
    // Usamos SupersetWithExercisesEntity para tener la información completa
    var supersets: Flow<List<SupersetWithExercisesEntity>> = supersetDao.getAllSupersets()

    @WorkerThread
    suspend fun insertSupersets(listSupersetEntity: List<SupersetEntity>) {
        Log.d(TAG, "Inserting supersets...")
        supersetDao.createSuperset(listSupersetEntity)
        supersets = supersetDao.getAllSupersets()
        Log.d(TAG, "Supersets inserted")
    }

    @WorkerThread
    suspend fun insertSingleSuperset(supersetEntity: SupersetEntity) {
        Log.d(TAG, "Inserting single superset...")
        supersetDao.createSuperset(supersetEntity)
        supersets = supersetDao.getAllSupersets()
        Log.d(TAG, "Superset inserted")
    }

    fun getSupersetsByUser(userId: Int): Flow<List<SupersetWithExercisesEntity>> {
        return supersetDao.getSupersetsByUser(userId)
    }

    @WorkerThread
    suspend fun updateExercise(exerciseEntity: ExerciseEntity) {
        Log.d(TAG, "Updating exercise...")
        exerciseDao.updateExercise(exerciseEntity)
        exercises = exerciseDao.getAllExercise()
        Log.d(TAG, "Exercise updated")
    }

    @WorkerThread
    suspend fun updateSuperset(supersetEntity: SupersetEntity) {
        Log.d(TAG, "Updating superset...")
        supersetDao.updateSuperset(supersetEntity)
        supersets = supersetDao.getAllSupersets()
        Log.d(TAG, "Superset updated")
    }

    @WorkerThread
    suspend fun deleteExercise(exerciseEntity: ExerciseEntity) {
        Log.d(TAG, "Deleting exercise...")
        exerciseDao.deleteExercise(exerciseEntity)
        exercises = exerciseDao.getAllExercise()
        Log.d(TAG, "Exercise deleted")
    }

    @WorkerThread
    suspend fun deleteSuperset(supersetEntity: SupersetEntity) {
        Log.d(TAG, "Deleting superset...")
        supersetDao.deleteSuperset(supersetEntity)
        supersets = supersetDao.getAllSupersets()
        Log.d(TAG, "Superset deleted")
    }
}