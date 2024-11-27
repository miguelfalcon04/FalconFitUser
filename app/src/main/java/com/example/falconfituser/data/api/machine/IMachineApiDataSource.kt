package com.example.falconfituser.data.api.machine

import com.example.falconfituser.data.machine.Machine
import retrofit2.Response

interface IMachineApiDataSource {
    suspend fun readAll(): Response<MachineListRaw>
    suspend fun readOne(id:Int): Response<Machine>
}