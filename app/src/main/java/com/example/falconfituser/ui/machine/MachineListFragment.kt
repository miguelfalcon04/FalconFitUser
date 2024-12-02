package com.example.falconfituser.ui.machine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.falconfituser.R
import com.example.falconfituser.databinding.FragmentMachineListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MachineListFragment : Fragment() {
    private lateinit var binding: FragmentMachineListBinding
    private val viewModel: MachineListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMachineListBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogin = view.findViewById<Button>(R.id.loginButton)
        val toRegister = view.findViewById<Button>(R.id.registerButton)

        val adapter = MachineListAdapter()
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

