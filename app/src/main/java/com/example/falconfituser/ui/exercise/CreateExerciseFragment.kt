package com.example.falconfituser.ui.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.data.api.exercise.ExerciseCreateData
import com.example.falconfituser.data.api.exercise.ExerciseRawAttributes
import com.example.falconfituser.databinding.FragmentCreateExerciseBinding
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class CreateExerciseFragment : Fragment() {
    private lateinit var binding: FragmentCreateExerciseBinding
    private val viewModel: CreateExerViewModel by viewModels()

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
                Toast.makeText(
                    requireContext(),
                    "Rellene todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                // Creo un ExerciseCreateData para pasarselo a la función
                val newExercise = ExerciseCreateData(
                    data = ExerciseRawAttributes(
                        title = title,
                        subtitle = subtitle,
                        description = description
                    )
                )

                // Llamo al createExercise() del ViewModel
                viewModel.createExercise(newExercise)

                // Y vuelvo a navegar a la lista
                findNavController().navigate(R.id.action_createExerciseFragment_to_exercise)
            }
        }
    }
}