package com.example.falconfituser.ui.superset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.falconfituser.R
import com.example.falconfituser.data.superset.Superset
import com.example.falconfituser.databinding.ItemSupersetBinding

class SupersetListAdapter(
    private val viewModel: SupersetListViewModel,
    private val navController: NavController
): ListAdapter<Superset, SupersetListAdapter.SupersetViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupersetViewHolder {
        val binding = ItemSupersetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SupersetViewHolder(binding, viewModel, navController)
    }

    override fun onBindViewHolder(holder: SupersetViewHolder, position: Int) {
        val superset = getItem(position)
        holder.bind(superset)
    }

    class SupersetViewHolder(
        private val binding: ItemSupersetBinding,
        private val viewModel: SupersetListViewModel,
        private val navController: NavController):
        RecyclerView.ViewHolder(binding.root){
            fun bind(superset: Superset){
                binding.supersetTitle.text = superset.title
                binding.supersetFirstEx.text = superset.exerciseOne?.title ?: "nulo"
                binding.supersetSecondEx.text = superset.exercisTwo?.title ?: "nulo"

                binding.btnDelete.setOnClickListener{
                    viewModel.deleteSuperset(superset.id.toInt())
                }

                binding.btnUpdate.setOnClickListener{
                    val supersetId = Bundle().apply {
                        putInt("supersetId", superset.id.toInt())
                        putString("supersetTitle", superset.title)
                    }

                    navController.navigate(R.id.createUpdateSupersetFragment, supersetId)
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