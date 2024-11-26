package com.example.falconfituser.data.db

import androidx.annotation.WorkerThread
import com.example.falconfituser.data.Machine
import com.example.falconfituser.data.db.machine.MachineDao
import com.example.falconfituser.data.db.machine.MachineEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FalconFitDBRepository @Inject constructor(
    private val machineDao: MachineDao
){
    val allMachines: Flow<List<MachineEntity>> = machineDao.getAll()


}