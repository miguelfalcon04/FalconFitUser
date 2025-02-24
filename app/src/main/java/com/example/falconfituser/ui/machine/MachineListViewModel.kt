package com.example.falconfituser.ui.machine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.local.LocalRepository
import com.example.falconfituser.data.machine.IMachineRepository
import com.example.falconfituser.data.machine.Machine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MachineListViewModel @Inject constructor(
    private val machineRepository: IMachineRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<MchnListUiState>(MchnListUiState.Loading)
    val uiState: StateFlow<MchnListUiState>
        get() = _uiState.asStateFlow()

    init {
        loadMachines()

        viewModelScope.launch{
            withContext(Dispatchers.Main){
                machineRepository.setStream.collect{ mchnList ->
                    if(mchnList.isEmpty()){
                        _uiState.value = MchnListUiState.Loading
                    }else{
                        _uiState.value = MchnListUiState.Success(mchnList)
                    }
                }
            }
        }
    }

    fun loadMachines(){
        viewModelScope.launch {
            val res = machineRepository.readAll()

            if(res.isNotEmpty()){
                MchnListUiState.Success(res)
            }else{
                MchnListUiState.Error("Error al obtener las máquinas")
            }
        }
    }

}


sealed class MchnListUiState(){
    data object Loading: MchnListUiState()
    class Success(val mchnList: List<Machine>): MchnListUiState()
    class Error(val message: String): MchnListUiState()
}