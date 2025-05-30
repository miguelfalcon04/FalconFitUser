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
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.superset.Superset
import com.example.falconfituser.databinding.FragmentCreateUpdateSupersetBinding
import com.google.android.material.snackbar.Snackbar
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

        setFields()

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

            lifecycleScope.launch{
                try {
                    val exerciseOneTitle = viewModel.searchExerciseName(selectedExercisesIds.first().toString())
                    val exerciseTwoTitle = viewModel.searchExerciseName(selectedExercisesIds.last().toString())

                    if (selectedExercisesIds.size > 2) {
                        Toast.makeText(requireContext(), getString(R.string.error_delete_exercises), Toast.LENGTH_SHORT).show()
                    } else if (selectedExercisesIds.size < 2) {
                        Toast.makeText(requireContext(), getString(R.string.error_add_exercises), Toast.LENGTH_SHORT).show()
                    } else {
                        if (title.isBlank()) {
                            Toast.makeText(requireContext(), getString(R.string.error_add_title), Toast.LENGTH_SHORT).show()
                        } else {

                            val createOrUpdateSuperset = Superset(
                                id = "1",
                                title = title,
                                exerciseOne = Exercise(
                                    id = selectedExercisesIds.first().toString(),
                                    title = exerciseOneTitle,
                                    subtitle = "NADA",
                                    description = "NADA",
                                    photo = "NADA"
                                ),
                                exercisTwo = Exercise(
                                    id = selectedExercisesIds.last().toString(),
                                    title = exerciseTwoTitle,
                                    subtitle = "NADA",
                                    description = "NADA",
                                    photo = "NADA"
                                )
                            )

                            val supersetId = arguments?.getInt("supersetId",-1)?:-1
                            val supersetDoc = arguments?.getString("supersetDocRef")
                            val supersetUserId = arguments?.getString("supersetUserId")

                            if(supersetId != -1){
                                viewModel.updateSuperset(
                                    supersetId,
                                    createOrUpdateSuperset.copy(
                                        document = supersetDoc,
                                        userId = supersetUserId)
                                )
                                makeSnackBar(R.string.supersetUpdated)
                            }else{
                                viewModel.createSuperset(createOrUpdateSuperset)
                                makeSnackBar(R.string.supersetCreated)
                            }

                            // Por último navego
                            findNavController().navigate(R.id.action_createUpdateSupersetFragment_to_superset)
                        }
                    }
                } catch (e: Exception){
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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

    private fun setFields(){
        arguments?.let { bundle ->
            if (bundle.getInt("supersetId", -1) != -1) {
                binding.addSupersetTitle.setText(bundle.getString("supersetTitle"))
            }
        }
    }

    private fun makeSnackBar(message: Int){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setAnchorView(R.id.bottom_navigation)
            .setAction("X"){ }
            .show()
    }
}