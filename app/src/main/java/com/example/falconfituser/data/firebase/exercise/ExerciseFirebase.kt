package com.example.falconfituser.data.firebase.exercise

import com.example.falconfituser.data.exercise.Exercise
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExerciseFirebase @Inject constructor() : IExerciseFirebase {

    private val firestore = FirebaseFirestore.getInstance()
    private val exercisesCollection = firestore.collection("exercises")

    override suspend fun createExercise(exercise: Exercise) {
        exercisesCollection.document().set(exercise)
    }

    override suspend fun getAllExercises(userId: Int): List<Exercise> {
        val exercises = exercisesCollection
                        .whereEqualTo("userId", userId)
                        .get()
                        .await()

        return exercises.documents.mapNotNull { document ->
            document.toObject(Exercise::class.java)
        }
    }

}