package com.example.falconfituser.ui.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.databinding.FragmentExerciseListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseListFragment : Fragment() {
    private lateinit var binding: FragmentExerciseListBinding
    private val viewModel: ExerciseListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExerciseListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCreateAnExercise = view.findViewById<FloatingActionButton>(R.id.createAnExercise)
        btnCreateAnExercise.setOnClickListener{
            findNavController().navigate(R.id.createExerciseFragment)
        }

        val adapter = ExerciseListAdapter()
        binding.exerciseList.adapter = adapter

        lifecycleScope.launch{
            viewModel.uiState.collect{uiState ->
                when(uiState){
                    ExercListUiState.Loading -> {
                    }
                    is ExercListUiState.Success -> {
                        adapter.submitList(uiState.exrcList)
                    }
                    is ExercListUiState.Error -> {
                    }
                }
            }
        }
    }

}