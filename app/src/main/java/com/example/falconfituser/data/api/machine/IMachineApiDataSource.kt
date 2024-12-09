package com.example.falconfituser.data.api.machine

import retrofit2.Response

interface IMachineApiDataSource {
    suspend fun readAll(): Response<MachineListRaw>
}