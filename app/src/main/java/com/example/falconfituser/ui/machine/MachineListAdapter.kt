package com.example.falconfituser.ui.machine

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.falconfituser.data.Machine
import com.example.falconfituser.databinding.FragmentMachineListBinding
import com.example.falconfituser.databinding.ItemMachineBinding

class MachineListAdapter {

    inner class MachineItemViewHolder(private val binding: ItemMachineBinding):
        ViewHolder(binding.root){

            fun bind(machine: Machine){
                binding.machineTitle.text = machine.title
            }
    }
}