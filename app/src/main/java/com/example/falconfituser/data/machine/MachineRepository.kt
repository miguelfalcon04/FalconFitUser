package com.example.falconfituser.data.machine

import com.example.falconfituser.data.api.machine.IMachineApiDataSource
import com.example.falconfituser.data.local.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MachineRepository @Inject constructor(
    private val apiData: IMachineApiDataSource,
    private val localRepository: LocalRepository
): IMachineRepository {

    private val _state = MutableStateFlow<List<Machine>>(listOf())
    override val setStream: StateFlow<List<Machine>>
        get() = _state.asStateFlow()

    override suspend fun readAll(): List<Machine> {
        try {
            val res = apiData.readAll()

            if(res.isSuccessful){
                val mchnList = res.body()?.data!!.toExternal()
                _state.value = mchnList

                for (machine in mchnList){
                    localRepository.createMachine(machine.toLocal())
                }

                return mchnList
            }
        }catch (e: Exception){
            val localMachines = localRepository.getMachines()
                .first()
                .map { it.toExternal() }

            _state.value = localMachines
        }

        return _state.value
    }

    override fun observeAll(): Flow<List<Machine>> {
        TODO("Not yet implemented")
    }
}