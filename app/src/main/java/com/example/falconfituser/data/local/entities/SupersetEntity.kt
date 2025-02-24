package com.example.falconfituser.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "superset")
data class SupersetEntity(
    @PrimaryKey val id: String,
    val title: String,
    val exerciseOneTitle: String,
    val exerciseTwoTitle: String,
    val userId: Int
)

