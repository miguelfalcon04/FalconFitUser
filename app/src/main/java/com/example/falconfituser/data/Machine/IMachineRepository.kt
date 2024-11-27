package com.example.falconfituser.data.Machine

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IMachineRepository {
    val setStream: StateFlow<List<Machine>>
    suspend fun readAll(): List<Machine>
    suspend fun readOne(id: Int): Machine
    fun observeAll(): Flow<List<Machine>>
}