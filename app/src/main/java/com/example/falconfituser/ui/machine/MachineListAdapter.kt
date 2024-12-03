package com.example.falconfituser.ui.machine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.falconfituser.data.machine.Machine
import com.example.falconfituser.databinding.ItemMachineBinding

class MachineListAdapter: ListAdapter<Machine,
        MachineListAdapter.MachineViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MachineViewHolder {
        val binding = ItemMachineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MachineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MachineViewHolder,position: Int){
        val machine = getItem(position)
        holder.bind(machine)
    }

    class MachineViewHolder(private val binding: ItemMachineBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(machine: Machine){
            binding.machineTitle.text = machine.title
            binding.machineSubtitle.text = machine.subtitle
            binding.machineDescription.text = machine.description
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Machine>() {
        override fun areItemsTheSame(oldItem: Machine, newItem: Machine): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Machine, newItem: Machine): Boolean {
            return oldItem == newItem
        }
    }
}
