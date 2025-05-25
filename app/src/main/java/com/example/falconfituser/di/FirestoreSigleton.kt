package com.example.falconfituser.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

/**
 * Singleton class for accessing FirebaseFirestore instance with persistence enabled.
 */
object FirestoreSigleton {
    private var instance: FirebaseFirestore? = null

    /**
     * Retrieves the singleton instance of FirebaseFirestore with persistence enabled.
     *
     * @return The singleton instance of FirebaseFirestore.
     */
    fun getInstance(): FirebaseFirestore {
        if (instance == null) {
            instance = FirebaseFirestore.getInstance().apply {
                firestoreSettings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build()
            }
        }
        return instance!!
    }
}