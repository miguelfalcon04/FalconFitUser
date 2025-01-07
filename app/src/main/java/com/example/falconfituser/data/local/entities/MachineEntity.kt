package com.example.falconfituser.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "machine")
data class MachineEntity (
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val photo: String?
)