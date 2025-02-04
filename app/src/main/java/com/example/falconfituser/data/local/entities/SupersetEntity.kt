package com.example.falconfituser.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.superset.Superset

@Entity(tableName = "superset",)
data class SupersetEntity(
    @PrimaryKey val id: String,
    val title: String,
    val exerciseOneId: String,
    val exerciseTwoId: String,
    val userId: Int
)

