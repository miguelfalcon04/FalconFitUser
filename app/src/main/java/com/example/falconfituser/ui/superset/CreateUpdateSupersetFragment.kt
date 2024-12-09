package com.example.falconfituser.ui.superset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.data.api.superset.ExercisePost
import com.example.falconfituser.data.api.superset.SupersetPost
import com.example.falconfituser.data.api.superset.SupersetRawPost
import com.example.falconfituser.databinding.FragmentCreateUpdateSupersetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateUpdateSupersetFragment : Fragment() {
    private lateinit var binding: FragmentCreateUpdateSupersetBinding
    private val viewModel: CreateUpdateSupersViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateUpdateSupersetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CreateUpdateSupersetAdapter()
        binding.exerciseList.adapter = adapter

        val btnBackToList = view.findViewById<Button>(R.id.backToSupersetListButton)
        btnBackToList.setOnClickListener{
            findNavController().navigate(R.id.action_createUpdateSupersetFragment_to_superset)
        }

        val btnCreateSuperset = view.findViewById<Button>(R.id.confirmSupersetButton)
        btnCreateSuperset.setOnClickListener{
            val title = binding.addSupersetTitle.text.toString()
            val selectedExercisesIds = adapter.exerToAdd

            // Verifico que los campos estan rellenos
                if (selectedExercisesIds.size > 2) {
                    Toast.makeText(requireContext(), "Elimine un ejercicio para continuar", Toast.LENGTH_SHORT).show()
                } else if (selectedExercisesIds.size < 2) {
                    Toast.makeText(requireContext(), "Añada más ejercicios para continuar", Toast.LENGTH_SHORT).show()
                } else {
                    if (title.isBlank()) {
                        Toast.makeText(requireContext(), "Añada un título", Toast.LENGTH_SHORT).show()
                    } else {
                        val createOrUpdateSuperset = SupersetPost(
                            data = SupersetRawPost(
                                title = title,
                                exercises = selectedExercisesIds.map { ExercisePost(it) }
                            )
                        )

                        val supersetId = arguments?.getInt("supersetId",-1)?:-1
                        if(supersetId != -1){
                            viewModel.updateSuperset(supersetId, createOrUpdateSuperset)
                        }else{
                            viewModel.createSuperset(createOrUpdateSuperset)
                        }

                        // Por último navego
                        findNavController().navigate(R.id.action_createUpdateSupersetFragment_to_superset)
                    }
                }


        }

        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is CreateUpdateSupersUiState.Error -> {
                    }
                    CreateUpdateSupersUiState.Loading -> {
                    }
                    is CreateUpdateSupersUiState.Success -> {
                        adapter.submitList(uiState.exrcList)
                    }
                }
            }
        }
    }
}