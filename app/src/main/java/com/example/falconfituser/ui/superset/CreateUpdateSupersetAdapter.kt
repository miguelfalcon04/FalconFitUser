package com.example.falconfituser.ui.superset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.databinding.ItemToListExerciseBinding


class CreateUpdateSupersetAdapter(
): ListAdapter<Exercise, CreateUpdateSupersetAdapter.CreateUpdateViewHolder>(DiffCallback()) {
    val exerToAdd: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateUpdateViewHolder {
        val binding = ItemToListExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CreateUpdateViewHolder(binding, exerToAdd)
    }

    override fun onBindViewHolder(holder: CreateUpdateViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }

    class CreateUpdateViewHolder(
        private val binding: ItemToListExerciseBinding,
        private val exerToAdd: MutableList<String> ):
        RecyclerView.ViewHolder(binding.root){
            fun bind(exercise: Exercise){
                binding.exerciseToLisTitle.text = exercise.title
                binding.exerciseToListSubtitle.text = exercise.subtitle

                binding.itemCheckbox.setOnClickListener{
                    if (exerToAdd.contains(exercise.document!!)) {
                        exerToAdd.remove(exercise.document)
                    } else {
                        exerToAdd.add(exercise.document)
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