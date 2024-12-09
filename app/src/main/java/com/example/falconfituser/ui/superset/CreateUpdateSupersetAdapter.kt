package com.example.falconfituser.ui.superset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.databinding.ItemToListExerciseBinding


class CreateUpdateSupersetAdapter(
    private val viewModel: CreateUpdateSupersViewModel
): ListAdapter<Exercise, CreateUpdateSupersetAdapter.CreateUpdateViewHolder>(DiffCallback()) {

    val exerToAdd: MutableList<Int> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateUpdateViewHolder {
        val binding = ItemToListExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CreateUpdateViewHolder(binding, viewModel, exerToAdd)
    }

    override fun onBindViewHolder(holder: CreateUpdateViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }

    class CreateUpdateViewHolder(
        private val binding: ItemToListExerciseBinding,
        private val viewModel: CreateUpdateSupersViewModel,
        private val exerToAdd: MutableList<Int> ):
        RecyclerView.ViewHolder(binding.root){
            fun bind(exercise: Exercise){
                binding.exerciseToLisTitle.text = exercise.title
                binding.exerciseToListSubtitle.text = exercise.subtitle

                binding.itemCheckbox.setOnClickListener{
                    if (exerToAdd.contains(exercise.id.toInt())) {
                        exerToAdd.remove(exercise.id.toInt())
                    } else {
                        exerToAdd.add(exercise.id.toInt())
                    }
                }
            }
        }


    class DiffCallback : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem == newItem
        }
    }
}