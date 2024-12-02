package com.example.falconfituser.data.machine

import com.example.falconfituser.data.api.machine.IMachineApiDataSource
import dagger.Module
import dagger.hilt.InstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

class MachineRepository @Inject constructor(
    private val apiData: IMachineApiDataSource,
): IMachineRepository {

    private val _state = MutableStateFlow<List<Machine>>(listOf(
        Machine(id = "1", title = "Banca", subtitle = "Pecho", description = "Muy chula me encanta mucho todo este como funciona puta strapi"),
        Machine(id = "2", title = "Extensiones"),
        Machine(id = "3", title = "Abdominales")
    ))
    override val setStream: StateFlow<List<Machine>>
        get() = _state.asStateFlow()

    override suspend fun readAll(): List<Machine> {
        val res = apiData.readAll()
        val mchn = _state.value.toMutableList()
        if(res.isSuccessful){
            val mchnList = res.body()?.data?:emptyList()
            _state.value = mchnList.toExternal()
        }
        else _state.value = mchn
        return mchn
    }

    override suspend fun readOne(id: Int): Machine {
        val res = apiData.readOne(id)
        return if(res.isSuccessful)res.body()!!
        else Machine("0","")
    }

    override fun observeAll(): Flow<List<Machine>> {
        TODO("Not yet implemented")
    }

    private fun refreshLocal(){
        runBlocking {
            val mchApi = apiData.readAll()
        }
    }

}