package com.example.falconfituser.ui.machine

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.falconfituser.R
import com.example.falconfituser.data.Machine
import com.example.falconfituser.databinding.FragmentMachineListBinding
import com.example.falconfituser.databinding.ItemMachineBinding
import kotlinx.coroutines.launch

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
        val adapter = MachineListAdapter(requireContext())
        val rv = binding.machineList
        rv.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect{
                    viewModel.uiState.collect{
                        adapter.submitList(it.machine)
                    }
                }
            }
        }
    }
}

