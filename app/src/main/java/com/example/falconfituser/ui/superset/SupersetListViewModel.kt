package com.example.falconfituser.ui.superset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.superset.ISupersetRepository
import com.example.falconfituser.data.superset.Superset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SupersetListViewModel @Inject constructor(
    private val supersetRepository: ISupersetRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<SupersListUiState>(SupersListUiState.Loading)
    val uiState: StateFlow<SupersListUiState>
        get() = _uiState.asStateFlow()

    fun deleteSuperset(supersetId: Int){
        viewModelScope.launch {
            supersetRepository.deleteSuperset(supersetId)
            withContext(Dispatchers.IO) {
                supersetRepository.readAll()
            }
        }
    }

    init{
        viewModelScope.launch{
            withContext(Dispatchers.Main){
                supersetRepository.setStream.collect{ supersList ->
                    if(supersList.isEmpty()){
                        _uiState.value = SupersListUiState.Loading
                    }else{
                        _uiState.value = SupersListUiState.Success(supersList)
                    }
                }
            }
        }
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                supersetRepository.readAll()
            }
        }
    }
}




sealed class SupersListUiState(){
    data object Loading: SupersListUiState()
    class Success(val supersList: List<Superset>): SupersListUiState()
    class Error(val message: String): SupersListUiState()
}