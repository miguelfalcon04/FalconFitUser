package com.example.falconfituser.ui.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.databinding.ItemExerciseBinding


class ExerciseListAdapter(
    private val viewModel: ExerciseListViewModel
): ListAdapter<Exercise,
        ExerciseListAdapter.ExerciseViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding, viewModel)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }

    class ExerciseViewHolder(
        private val binding: ItemExerciseBinding,
        private val viewModel: ExerciseListViewModel):
        RecyclerView.ViewHolder(binding.root){
        fun bind(exercise: Exercise){
            binding.exerciseTitle.text = exercise.title
            binding.exerciseSubtitle.text = exercise.subtitle
            binding.exerciseDescription.text = exercise.description
            binding.btnDelete.setOnClickListener{
                viewModel.deleteExercise(exercise.id.toInt())
            }
            binding.btnUpdate.setOnClickListener{
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