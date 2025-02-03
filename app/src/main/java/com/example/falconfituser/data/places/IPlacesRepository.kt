package com.example.falconfituser.data.places

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IPlacesRepository {
    val setStream: StateFlow<List<Places>>
    suspend fun readAll(): List<Places>
    fun observeAll(): Flow<List<Places>>
}