package com.example.falconfituser.data.machine

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IMachineRepository {
    val setStream: StateFlow<List<Machine>>
    suspend fun readAll(): List<Machine>
    fun observeAll(): Flow<List<Machine>>
}