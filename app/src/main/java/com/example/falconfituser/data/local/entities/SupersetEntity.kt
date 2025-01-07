package com.example.falconfituser.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.superset.Superset

@Entity(
    tableName = "superset",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseOneId"]
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["id"],
            childColumns = ["exerciseTwoId"]
        )
    ]
)
data class SupersetEntity(
    @PrimaryKey val id: String,
    val title: String,
    val exerciseOneId: String,
    val exerciseTwoId: String,
    val userId: Int
)

data class SupersetWithExercisesEntity(
    val id: String,
    val title: String,
    val exerciseOneId: String,
    val exerciseTwoId: String,
    val userId: Int,
    // Datos del primer ejercicio
    val exerciseOneTitle: String,
    val exerciseOneSubtitle: String,
    val exerciseOneDescription: String,
    // Datos del segundo ejercicio
    val exerciseTwoTitle: String,
    val exerciseTwoSubtitle: String,
    val exerciseTwoDescription: String
) {
    fun asSuperset(): Superset {
        return Superset(
            id = id,
            title = title,
            exerciseOne = Exercise(
                id = exerciseOneId,
                title = exerciseOneTitle,
                subtitle = exerciseOneSubtitle,
                description = exerciseOneDescription
            ),
            exercisTwo = Exercise(
                id = exerciseTwoId,
                title = exerciseTwoTitle,
                subtitle = exerciseTwoSubtitle,
                description = exerciseTwoDescription
            )
        )
    }
}

fun List<SupersetWithExercisesEntity>.asSupersetList(): List<Superset> {
    return this.map { it.asSuperset() }
}