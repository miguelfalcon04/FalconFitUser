package com.example.falconfituser.ui.superset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.falconfituser.data.superset.Superset
import com.example.falconfituser.databinding.ItemSupersetBinding

class SupersetListAdapter(
    private val viewModel: SupersetListViewModel
): ListAdapter<Superset, SupersetListAdapter.SupersetViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupersetViewHolder {
        val binding = ItemSupersetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SupersetViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: SupersetViewHolder, position: Int) {
        val superset = getItem(position)
        holder.bind(superset)
    }

    class SupersetViewHolder(
        private val binding: ItemSupersetBinding,
        private val viewModel: SupersetListViewModel):
        RecyclerView.ViewHolder(binding.root){
        fun bind(superset: Superset){
            binding.supersetTitle.text = superset.title

            binding.btnDelete.setOnClickListener{
                viewModel.deleteSuperset(superset.id.toInt())
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Superset>() {
        override fun areItemsTheSame(oldItem: Superset, newItem: Superset): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Superset, newItem: Superset): Boolean {
            return oldItem == newItem
        }
    }
}