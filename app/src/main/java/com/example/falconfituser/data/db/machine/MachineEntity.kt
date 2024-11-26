package com.example.falconfituser.data.db.machine

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.falconfituser.data.Machine

@Entity(tableName = "machine")
data class MachineEntity (
    @PrimaryKey val id: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    )

fun List<MachineEntity>.asMachine(): List<Machine>{
    return this.map{
        Machine(
            it.id,
            it.title.replaceFirstChar { c ->c.uppercase() },
            it.subtitle,
            it.description
        )
    }
}