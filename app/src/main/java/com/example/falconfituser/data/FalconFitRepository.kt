package com.example.falconfituser.data

import com.example.falconfituser.data.api.FalconFitApiRepository
import com.example.falconfituser.data.db.FalconFitDBRepository
import com.example.falconfituser.data.db.machine.asMachine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
    //private val dbRepository: FalconFitDBRepository
) {
    //val machine: Flow<List<Machine>>
        //get(){
            //val list = dbRepository.allMachines.map {
                //it.asMachine()
            //}
            //return list
        //}

    suspend fun refreshList(){
        withContext(Dispatchers.IO){
            val apiMachine = apiRepository.getAll()
        }
    }
}