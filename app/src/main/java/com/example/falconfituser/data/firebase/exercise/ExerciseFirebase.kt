package com.example.falconfituser.data.firebase.exercise

import com.example.falconfituser.data.exercise.Exercise
import com.google.firebase.firestore.FirebaseFirestore

class ExerciseFirebase : IExerciseFirebase {

    private val firestore = FirebaseFirestore.getInstance()
    private val exercisesCollection = firestore.collection("exercises")

    override suspend fun createExercise(exercise: Exercise) {
        exercisesCollection.document().set(exercise)
    }

}