package com.example.falconfituser.ui.machine

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.falconfituser.data.machine.Machine
import com.example.falconfituser.databinding.ItemMachineBinding

class MachineListAdapter: ListAdapter<Machine, MachineListAdapter.MachineItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MachineItemViewHolder {
        val binding = ItemMachineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MachineItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MachineItemViewHolder,position: Int){
        val machine = getItem(position)
        holder.bind(machine)
    }

    class MachineItemViewHolder(private val binding: ItemMachineBinding):
        ViewHolder(binding.root){
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
