package com.example.falconfituser.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise")
data class ExerciseEntity (
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val userId: Int
)