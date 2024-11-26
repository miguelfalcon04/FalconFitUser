package com.example.falconfituser.data

import com.example.falconfituser.data.api.FalconFitApiRepository
import com.example.falconfituser.data.db.FalconFitDBRepository
import com.example.falconfituser.data.db.machine.asMachine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject
import javax.inject.Singleton

// Tiene un singleton porque solo debe haber una instancia del mismo
@Singleton
class FalconFitRepository @Inject constructor(
    //el repositorio es el encargado de manejar tanto el repositorio de la base de datos como el repositorio de la api

    private val apiRepository: FalconFitApiRepository,
    // private val dbRepository: FalconFitDBRepository
) {
    //val machine: Flow<List<Machine>>
        //get(){
            //val list = dbRepository.allMachines.map {
                //it.asMachine()
            //}
            //return list
        //}
    private val _state = MutableStateFlow<List<Machine>>(listOf())
    override val setStream: StateFlow<List<Machine>>
        get() = _state.asStateFlow()

    override suspend fun readAll(): List<Pokemon> {
        val response = remoteDataSource.readAll()
        val pokemonList = _state.value.toMutableList()
        if(response.isSuccessful){
            val pokemonRawList = response.body()!!.results
            pokemonRawList.forEach{ pkr ->
                pokemonList.add(readOne(idUrl(pkr.url)!!))
                _state.emit(pokemonList.toList())

            }
        }
        else _state.value = pokemonList

        return pokemonList
    }
    suspend fun refreshList(){
        withContext(Dispatchers.IO){
            val apiMachine = apiRepository.getAll()
        }
    }
}