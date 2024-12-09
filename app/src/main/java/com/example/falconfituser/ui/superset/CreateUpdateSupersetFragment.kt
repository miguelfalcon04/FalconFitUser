package com.example.falconfituser.ui.superset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.falconfituser.R
import com.example.falconfituser.databinding.FragmentCreateUpdateSupersetBinding
import dagger.hilt.android.AndroidEntryPoint

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

        val btnBackToList = view.findViewById<Button>(R.id.backToSupersetListButton)
        btnBackToList.setOnClickListener{
            findNavController().navigate(R.id.action_createUpdateSupersetFragment_to_superset)
        }

        val btnCreateSuperset = view.findViewById<Button>(R.id.confirmSupersetButton)
        btnCreateSuperset.setOnClickListener{

        }
    }

}