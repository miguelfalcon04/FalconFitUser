package com.example.falconfituser.ui.superset

import android.content.Context
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SupersetListAdapter(
    private val viewModel: SupersetListViewModel,
    private val navController: NavController,
    private val context: Context
): ListAdapter<Superset, SupersetListAdapter.SupersetViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupersetViewHolder {
        val binding = ItemSupersetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SupersetViewHolder(binding, viewModel, navController, this)
    }

    override fun onBindViewHolder(holder: SupersetViewHolder, position: Int) {
        val superset = getItem(position)
        holder.bind(superset)
    }

    class SupersetViewHolder(
        private val binding: ItemSupersetBinding,
        private val viewModel: SupersetListViewModel,
        private val navController: NavController,
        private val adapter: SupersetListAdapter
    ):
        RecyclerView.ViewHolder(binding.root){
            fun bind(superset: Superset){
                binding.supersetTitle.text = superset.title
                binding.supersetFirstEx.text = superset.exerciseOne?.title ?: "nulo"
                binding.supersetSecondEx.text = superset.exercisTwo?.title ?: "nulo"

                binding.btnDelete.setOnClickListener{
                    adapter.deleteOrNot(superset)
                }

                binding.btnUpdate.setOnClickListener{
                    val supersetId = Bundle().apply {
                        putInt("supersetId", superset.id!!.toInt())
                        putString("supersetTitle", superset.title)
                        putString("supersetDocRef", superset.document)
                        putString("supersetUserId", superset.userId)
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

    fun deleteOrNot(superset: Superset){
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.deleteSuperset))
            .setNegativeButton(context.getString(R.string.no), null)
            .setPositiveButton(context.getString(R.string.yes)) { dialog, which ->
                viewModel.deleteSuperset(superset.id!!.toInt(), superset.document!!)
            }
            .show()
    }
}