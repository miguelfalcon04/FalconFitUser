package com.example.falconfituser.data.db

import com.example.falconfituser.data.Workout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkoutLocalDatabase @Inject constructor(
    private val dao: WorkoutDao
): WorkoutLocalDataSource {

    override suspend fun insert(workout: Workout) {
        dao.create(workout.toLocal())
    }

    override suspend fun update(workout: Workout) {
        dao.update(workout.toLocal())
    }

    override suspend fun readAll(): List<Workout> {
        return dao.readAll().toExternal()
    }

    override suspend fun delete(workout: Workout) {
        dao.delete(workout.toLocal())
    }

    override fun observeALl(): Flow<List<Workout>> {
        return dao.observeAll().map {
            localWorkouts -> localWorkouts.toExternal()
        }
    }
}