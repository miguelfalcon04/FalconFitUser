package com.example.falconfituser.ui.machine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.falconfituser.databinding.FragmentMachineListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MachineListFragment : Fragment() {
    private lateinit var binding: FragmentMachineListBinding
    private val viewModel: MachineListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMachineListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MachineListAdapter(viewModel)
        binding.machineList.adapter = adapter

        lifecycleScope.launch{
            viewModel.uiState.collect{uiState ->
                when(uiState){
                    MchnListUiState.Loading -> {
                    }
                    is MchnListUiState.Success -> {
                        adapter.submitList(uiState.mchnList)
                    }
                    is MchnListUiState.Error -> {
                    }
                }
            }
        }
    }

}

