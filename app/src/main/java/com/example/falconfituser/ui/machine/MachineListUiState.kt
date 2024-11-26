package com.example.falconfituser.ui.machine

import com.example.falconfituser.data.Machine

data class MachineListUiState (
    val machine: List<Machine>,
    val errorMessage: String?=null
)