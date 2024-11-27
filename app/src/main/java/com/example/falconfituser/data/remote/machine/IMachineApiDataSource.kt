package com.example.falconfituser.data.remote.machine

import com.example.falconfituser.data.Machine.Machine
import retrofit2.Response

interface IMachineApiDataSource {
    suspend fun readAll(): Response<MachineListRaw>
    suspend fun readOne(id:Int): Response<Machine>
}