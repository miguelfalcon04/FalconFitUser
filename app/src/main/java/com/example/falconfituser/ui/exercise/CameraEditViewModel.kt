package com.example.falconfituser.ui.exercise

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state of the captured photo.
 * It holds a [Uri] reference to the image and updates it when a new photo is captured.
 */
@HiltViewModel
class CameraEditViewModel @Inject constructor(): ViewModel(){
    private val _photo = MutableStateFlow<Uri>(Uri.EMPTY)
    val photo: StateFlow<Uri>
        get() = _photo.asStateFlow()

    /**
     * Updates the photo URI when an image is captured.
     * Only updates the state if the URI is not null.
     *
     * @param uri The [Uri] of the captured image
     */
    fun onImageCapture(uri: Uri?){
        viewModelScope.launch {
            uri?.let {
                _photo.value = uri
            }
        }
    }
}