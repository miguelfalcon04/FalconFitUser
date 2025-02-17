package com.example.falconfituser.ui.machine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.local.LocalRepository
import com.example.falconfituser.data.machine.IMachineRepository
import com.example.falconfituser.data.machine.Machine
import com.example.falconfituser.data.machine.toLocal
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
    private val localRepository: LocalRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow<MchnListUiState>(MchnListUiState.Loading)
    val uiState: StateFlow<MchnListUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch{
            withContext(Dispatchers.Main){
                machineRepository.setStream.collect{
                        mchnList ->
                    if(mchnList.isEmpty()){
                        _uiState.value = MchnListUiState.Loading
                    }else{
                        _uiState.value = MchnListUiState.Success(mchnList)
                    }
                }
            }
        }
        loadMachines()
    }

    fun loadMachines(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val res = machineRepository.readAll()

                if(!res.isEmpty()){
                    for(machine in res){
                        localRepository.createMachine(machine.toLocal())
                    }
                }
            }
        }
    }

}


sealed class MchnListUiState(){
    data object Loading: MchnListUiState()
    class Success(val mchnList: List<Machine>): MchnListUiState()
    class Error(val message: String): MchnListUiState()
}