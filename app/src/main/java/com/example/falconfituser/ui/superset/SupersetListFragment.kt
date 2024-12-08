package com.example.falconfituser.ui.superset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.falconfituser.databinding.FragmentSupersetListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SupersetListFragment : Fragment() {
    private lateinit var binding: FragmentSupersetListBinding
    private val viewModel: SupersetListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupersetListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SupersetListAdapter(viewModel)
        binding.supersetList.adapter = adapter

        lifecycleScope.launch{
            viewModel.uiState.collect{uiState ->
                when(uiState){
                    SupersListUiState.Loading -> {
                    }
                    is SupersListUiState.Success -> {
                        adapter.submitList(uiState.supersList)
                    }
                    is SupersListUiState.Error -> {
                    }
                }
            }
        }
    }
}