package com.example.falconfituser.ui.superset

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.local.LocalRepository
import com.example.falconfituser.data.superset.ISupersetRepository
import com.example.falconfituser.data.superset.Superset
import com.example.falconfituser.data.superset.toExternal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SupersetListViewModel @Inject constructor(
    private val supersetRepository: ISupersetRepository,
    private val localRepository: LocalRepository,
    private val sharedPreferences: SharedPreferences
    ): ViewModel() {
    private val _uiState = MutableStateFlow<SupersListUiState>(SupersListUiState.Loading)
    val uiState: StateFlow<SupersListUiState>
        get() = _uiState.asStateFlow()

    // Explicación en ExerciseListViewModel
    private var collectionJob: Job? = null

    fun initialize() {
        collectionJob?.cancel()

        collectionJob = viewModelScope.launch(Dispatchers.Main) {  // Cambiamos el dispatcher aquí
            try {
                launch {
                    supersetRepository.setStream.collect { supersList ->
                        if (supersList.isEmpty()) {
                            _uiState.value = SupersListUiState.Loading
                        } else {
                            _uiState.value = SupersListUiState.Success(supersList)
                        }
                    }
                }

                withContext(Dispatchers.IO) {
                    val userId = sharedPreferences.getString("USER_ID", null)?.toIntOrNull()
                    if (userId != null) {
                        loadSupersets()
                    } else {
                        _uiState.value = SupersListUiState.Error("ID de usuario no encontrado")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = SupersListUiState.Error("Error al cargar los supersets: ${e.message}")
            }
        }
    }

    fun loadSupersets(){
        viewModelScope.launch {
            val userId = sharedPreferences.getString("USER_ID", null)?.toIntOrNull() ?: 0

            val remoteSuperset: List<Superset> =
                try {
                    withContext(Dispatchers.IO) {
                        supersetRepository.readAll(userId)
                    }
                }catch (e: Exception){
                    emptyList()
                }

            if(remoteSuperset.isNotEmpty()){
                _uiState.value = SupersListUiState.Success(remoteSuperset)
            } else {
                withContext(Dispatchers.IO){
                    localRepository.getSupersetsByUser(userId).collect{ localSupersetEntity ->
                        _uiState.value = SupersListUiState.Success(
                            localSupersetEntity.map { it.toExternal() }
                        )
                    }
                }
            }
        }
    }

    fun deleteSuperset(supersetId: Int){
        viewModelScope.launch {
            supersetRepository.deleteSuperset(supersetId)
            withContext(Dispatchers.IO) {
                loadSupersets()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        collectionJob?.cancel() // Limpio al destruir el ViewModel
    }

}

sealed class SupersListUiState(){
    data object Loading: SupersListUiState()
    class Success(val supersList: List<Superset>): SupersListUiState()
    class Error(val message: String): SupersListUiState()
}