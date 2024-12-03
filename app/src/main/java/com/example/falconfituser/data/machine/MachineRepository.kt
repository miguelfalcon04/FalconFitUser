package com.example.falconfituser.data.machine

import com.example.falconfituser.data.api.machine.IMachineApiDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MachineRepository @Inject constructor(
    private val apiData: IMachineApiDataSource,
): IMachineRepository {

    private val _state = MutableStateFlow<List<Machine>>(listOf())
    override val setStream: StateFlow<List<Machine>>
        get() = _state.asStateFlow()

    override suspend fun readAll(): List<Machine> {
        val res = apiData.readAll()
        val machin = _state.value.toMutableList()
        if(res.isSuccessful){
            val mchnList = res.body()?.data?:emptyList()
            _state.value = mchnList.toExternal()
        }
        else _state.value = machin
        return machin
    }

    override suspend fun readOne(id: Int): Machine {
        val res = apiData.readOne(id)
        return if(res.isSuccessful)res.body()!!
        else Machine("0","fuera","no","furula")
    }

    override fun observeAll(): Flow<List<Machine>> {
        TODO("Not yet implemented")
    }
}