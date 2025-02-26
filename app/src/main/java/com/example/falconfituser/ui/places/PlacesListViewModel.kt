package com.example.falconfituser.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falconfituser.data.places.IPlacesRepository
import com.example.falconfituser.data.places.Places
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlacesListViewModel @Inject constructor(
    private val placesRepository: IPlacesRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<PlacesListUiState>(PlacesListUiState.Loading)
    val uiState: StateFlow<PlacesListUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch{
            loadPlaces()

            withContext(Dispatchers.Main){
                placesRepository.setStream.collect{ placeList ->
                    if(placeList.isEmpty()){
                        _uiState.value = PlacesListUiState.Loading
                    }else{
                        _uiState.value = PlacesListUiState.Success(placeList)
                    }
                }
            }
        }
    }

    suspend fun loadPlaces(): List<Places> {
        return placesRepository.readAll()
    }
}

sealed class PlacesListUiState(){
    data object Loading: PlacesListUiState()
    class Success(val placeList: List<Places>): PlacesListUiState()
    class Error(val message: String): PlacesListUiState()
}