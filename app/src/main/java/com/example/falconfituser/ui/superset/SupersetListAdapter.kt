package com.example.falconfituser.ui.superset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.data.superset.Superset
import com.example.falconfituser.databinding.ItemSupersetBinding
import com.example.falconfituser.databinding.ItemToListExerciseBinding

class SupersetListAdapter(
    private val viewModel: SupersetListViewModel
): ListAdapter<Superset, SupersetListAdapter.SupersetViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupersetViewHolder {
        val bindingList = ItemToListExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val binding = ItemSupersetBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SupersetViewHolder(bindingList, binding, viewModel)
    }

    override fun onBindViewHolder(holder: SupersetViewHolder, position: Int) {
        val superset = getItem(position)
        holder.bind(superset)
    }

    class SupersetViewHolder(
        private val bindingList: ItemToListExerciseBinding,
        private val binding: ItemSupersetBinding,
        private val viewModel: SupersetListViewModel):
        RecyclerView.ViewHolder(binding.root){
            fun  bindingExer(exercise: Exercise){
                var idEje: Int
                bindingList.itemTitle.text = exercise.title
                bindingList.itemSubtitle.text = exercise.subtitle

                bindingList.itemCheckbox.setOnClickListener{
                    idEje = exercise.id.toInt()
                }
            }

            fun bind(superset: Superset){
                binding.supersetTitle.text = superset.title
                binding.supersetFirstEx.text = superset.exerciseOne.title
                binding.supersetSecondEx.text = superset.exercisTwo.title

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