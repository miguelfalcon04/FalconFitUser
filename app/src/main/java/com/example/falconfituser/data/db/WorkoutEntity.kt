package com.example.falconfituser.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout")
data class WorkoutEntity (
    @PrimaryKey val id: Int,
                val title: String,
                val subtitle: String,
                val machineIds: String //Ids separados por coma para poder almacenarlos
)