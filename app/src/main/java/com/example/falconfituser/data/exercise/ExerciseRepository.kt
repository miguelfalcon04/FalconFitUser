package com.example.falconfituser.data.exercise

import com.example.falconfituser.data.api.exercise.IExerciseApiDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    private val apiData: IExerciseApiDataSource
): IExerciseRepository {

    private val _state = MutableStateFlow<List<Exercise>>(listOf())
    override val setStream: StateFlow<List<Exercise>>
        get() = _state.asStateFlow()

    override suspend fun readAll(): List<Exercise> {
        val res = apiData.readAll()
        val exerc = _state.value.toMutableList()
        if(res.isSuccessful){
            val exercList = res.body()?.data?:emptyList()
            _state.value = exercList.toExternal()
        }
        else _state.value = exerc
        return exerc
    }

    override suspend fun readOne(id: Int): Exercise {
        val res = apiData.readOne(id)
        return if(res.isSuccessful)res.body()!!
        else Exercise("0","fuera","no","furula")
    }

    override fun observeAll(): Flow<List<Exercise>> {
        TODO("Not yet implemented")
    }
}