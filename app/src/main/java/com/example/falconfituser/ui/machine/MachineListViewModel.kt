package com.example.falconfituser.ui.machine

import com.example.falconfituser.data.Machine.IMachineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MachineListViewModel @Inject constructor(
    private val machineRepository: IMachineRepository
) {

}