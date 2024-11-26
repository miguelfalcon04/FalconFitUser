package com.example.falconfituser.ui.machine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.FalconFitRepository
import com.example.falconfituser.data.Machine
import com.example.falconfituser.data.api.FalconFitApiRepository
import com.example.falconfituser.ui.machine.MachineListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class MachineListViewModel @Inject constructor(
    private val repository: FalconFitRepository
): ViewModel(){
    private val _uiState = MutableStateFlow(MachineListUiState(listOf()))
    val uiState: StateFlow<MachineListUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch{
            try {
                repository.refreshList()
            } catch (e: IOException){
                _uiState.value = _uiState.value.copy(errorMessage = e.message!!)
            }
        }

        // viewModelScope.launch{
        //    repository.machine.collect{
        //        _uiState.value = MachineListUiState(it)
        //    }
        //}
    }
}
