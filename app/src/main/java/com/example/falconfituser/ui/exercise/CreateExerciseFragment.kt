package com.example.falconfituser.ui.exercise

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.falconfituser.R
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.databinding.FragmentCreateExerciseBinding
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue


private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA,
                                            Manifest.permission.RECORD_AUDIO)
@AndroidEntryPoint
class CreateExerciseFragment: Fragment() {
    private var _photoUri:Uri? = null
    private lateinit var binding: FragmentCreateExerciseBinding
    private val viewModel: CreateExerViewModel by viewModels()
    private lateinit var sharedPreferences: SharedPreferences
    private val viewModelCamera: CameraEditViewModel by activityViewModels()

    // Se declara el contrato
    val contract = ActivityResultContracts.RequestMultiplePermissions()

    // Recorro la lista de permisos y comprueba si han sido concedidos
    // Si todos han sido concedidos navegamos a la camara, sino de un mensaje de error
    val launcher = registerForActivityResult(contract){
        permissions ->
        var granted = true
        permissions.entries.forEach{
            permission ->
                if(permission.key in PERMISSIONS_REQUIRED && !permission.value){
                    granted = false
                }
        }
        if(granted){
            findNavController().navigate(R.id.action_createExerciseFragment_to_cameraPreviewFragment)
        }else{
            Toast.makeText(requireContext(), "No tiene permisos de camara", Toast.LENGTH_LONG).show()
        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                loadPhoto(uri)
            }
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateExerciseBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Rellena los campos con el ejercicio
        setFields()

        // Inicializa sharedPreferences
        sharedPreferences = requireContext().getSharedPreferences("falcon_fit_prefs", 0)

        binding.showCamera.setOnClickListener{
            if(hasCameraPermissions(requireContext())){ // Si hay permisos navegamos a la camara
                findNavController().navigate(R.id.action_createExerciseFragment_to_cameraPreviewFragment)
            }else { // Si no los hay se piden
                launcher.launch(PERMISSIONS_REQUIRED)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModelCamera.photo.collect{
                    photoUri ->
                        when(photoUri){
                            Uri.EMPTY -> {}
                            else -> {
                                loadPhoto(photoUri)
                            }
                        }
                }
            }
        }

        // Se abre el desplegable para seleccionar una foto
        binding.selectPhotoBtn.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        // Vuelve al listado de fotos
        val btnBackToList = view.findViewById<MaterialButton>(R.id.backToExerciseListButton)
        btnBackToList.setOnClickListener{
            findNavController().navigate(R.id.action_createExerciseFragment_to_exercise)
        }


        val btnCreateExer = view.findViewById<Button>(R.id.createExerciseButton)
        btnCreateExer.setOnClickListener{
            // Obtengo los valores del formulario
            val title = binding.titleEditText.text.toString()
            val subtitle = binding.subtitleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()

            // Verifico que los campos estan rellenos
            if (title.isBlank() || subtitle.isBlank() || description.isBlank()) {
                Toast.makeText(requireContext(),getString(R.string.error_fill_all),
                    Toast.LENGTH_SHORT).show()
            }else{

                // Creo un Exercise para pasarselo a la función
                val exerciseToSend = Exercise(
                    id = "1",
                    title = title,
                    subtitle = subtitle,
                    description = description,
                    photo = "NADA"
                )

                // Si exerciseId es != -1 significa que he navegado desde ListAdapter
                // queriendo hacer un update
                val exerciseId = arguments?.getInt("exerciseId",-1)?: -1
                val exerciseDocRef = arguments?.getString("exerciseDocRef")
                val exercisePhoto = arguments?.getString("exercisePhoto")
                val exerciseUserId = arguments?.getString("exerciseUserId")

                if(exerciseId != -1){
                    viewModel.updateExercise(
                        exerciseId,
                        exerciseToSend.copy(
                            photo = exercisePhoto,
                            document = exerciseDocRef!!,
                            userId = exerciseUserId
                        ),
                        _photoUri
                    )
                }else{
                    // Llamo al createExercise() del ViewModel
                    viewModel.createExercise(exerciseToSend, _photoUri)
                }

                // Y vuelvo a navegar a la lista
                findNavController().navigate(R.id.action_createExerciseFragment_to_exercise)
            }
        }
    }

    private fun loadPhoto(uri:Uri?) {
        binding.exerciseImage.load(uri)
        _photoUri = uri
    }

    // Comprobar si tenemos permiso
    private fun hasCameraPermissions(context: Context):Boolean{
        return PERMISSIONS_REQUIRED.all{
            permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun setFields(){
        // Recuperamos todos los datos del Bundle
        arguments?.let { bundle ->
            // Solo rellenamos si existe un exerciseId (significa que venimos para actualizar)
            if (bundle.getInt("exerciseId", -1) != -1) {
                binding.titleEditText.setText(bundle.getString("exerciseTitle"))
                binding.subtitleEditText.setText(bundle.getString("exerciseSubtitle"))
                binding.descriptionEditText.setText(bundle.getString("exerciseDescription"))

                // Si hay una foto, la cargamos
                bundle.getString("exercisePhoto")?.let { photoUrl ->
                    binding.exerciseImage.load(photoUrl)
                }
            }
        }
    }
}