package com.example.falconfituser.ui.machine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.falconfituser.R
import com.example.falconfituser.data.machine.Machine
import com.example.falconfituser.databinding.ItemMachineBinding

class MachineListAdapter(
    private val viewModel: MachineListViewModel
): ListAdapter<Machine,
        MachineListAdapter.MachineViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MachineViewHolder {
        val binding = ItemMachineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MachineViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: MachineViewHolder,position: Int){
        val machine = getItem(position)
        holder.bind(machine)
    }

    class MachineViewHolder(
        private val binding: ItemMachineBinding,
        private val viewModel: MachineListViewModel):
        RecyclerView.ViewHolder(binding.root){
        fun bind(machine: Machine){
            binding.machineTitle.text = machine.title
            binding.machineSubtitle.text = machine.subtitle
            binding.machineDescription.text = machine.description
            if(machine.picture != null){
                binding.machineImage.load(machine.picture)
            }else{
                binding.machineImage.load(R.drawable.help)
            }

            binding.machineButton.setOnClickListener{
                viewModel.loadMachines()
            }
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
