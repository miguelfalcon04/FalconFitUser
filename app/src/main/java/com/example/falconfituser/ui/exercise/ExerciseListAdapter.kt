package com.example.falconfituser.ui.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.falconfituser.R
import com.example.falconfituser.data.exercise.Exercise
import com.example.falconfituser.databinding.ItemExerciseBinding


class ExerciseListAdapter(
    private val viewModel: ExerciseListViewModel,
    private val navController: NavController
): ListAdapter<Exercise,
        ExerciseListAdapter.ExerciseViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding, viewModel, navController)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }

    class ExerciseViewHolder(
        private val binding: ItemExerciseBinding,
        private val viewModel: ExerciseListViewModel,
        private val navController: NavController
        ):
        RecyclerView.ViewHolder(binding.root){
        fun bind(exercise: Exercise){
            binding.exerciseTitle.text = exercise.title
            binding.exerciseSubtitle.text = exercise.subtitle
            binding.exerciseDescription.text = exercise.description
            binding.btnDelete.setOnClickListener{
                viewModel.deleteExercise(exercise.id.toInt())
            }
            // PARA DAVID -> No se si has explicado lo de Bundle, lo he sacado de stack over flow
            binding.btnUpdate.setOnClickListener{ // Si pulsamos este boton obtenemos el id del ejercicio en especifico y navegamos al createExerciseFragment, recogemos el id y si es != null entonces hacemos un update, si es == null entonces unicamente hace un post
                val exerciseId = Bundle();
                exerciseId.putInt("exerciseId", exercise.id.toInt())
                navController.navigate(R.id.createExerciseFragment, exerciseId)
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