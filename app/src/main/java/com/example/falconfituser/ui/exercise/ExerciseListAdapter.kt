package com.example.falconfituser.ui.exercise

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
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
            if(exercise.photo!=null){
                binding.exerciseImage.load(exercise.photo)
            }else{
                binding.exerciseImage.load(R.drawable.help)
            }

            binding.btnDelete.setOnClickListener{
                viewModel.deleteExercise(exercise.id!!.toInt(), exercise.document!!)
            }

            // PARA DAVID -> No se si has explicado lo de Bundle, lo he sacado de stack over flow
            // Si pulsamos este boton obtenemos el id del ejercicio en especifico y navegamos al
            // createExerciseFragment, recogemos el id y si es != null entonces hacemos un update,
            // si es == null entonces unicamente hace un post
            binding.btnUpdate.setOnClickListener {
                val exerciseData = Bundle().apply {
                    putInt("exerciseId", exercise.id!!.toInt())
                    putString("exerciseTitle", exercise.title)
                    putString("exerciseSubtitle", exercise.subtitle)
                    putString("exerciseDescription", exercise.description)
                    putString("exercisePhoto", exercise.photo)
                    putString("exerciseDocRef", exercise.document)
                }
                navController.navigate(R.id.createExerciseFragment, exerciseData)
            }

            // Realizado con la documentación de
            // https://developer.android.com/guide/components/intents-filters?hl=es-419#Building
            binding.btnShare.setOnClickListener{
                val textMessage = "¡Mira este ejercicio! ${exercise.title} - " +
                        "${exercise.subtitle}: ${exercise.description}"

                val sendIntent = Intent().apply{
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, textMessage)
                    type = "text/plain"
                }

                try{
                    binding.root.context.startActivity(Intent.createChooser
                        (sendIntent, "Compartir usando"))
                }catch (e: Exception){
                    Toast.makeText(binding.root.context,"Se produjo algún error, intentelo " +
                            "mas tarde", Toast.LENGTH_SHORT).show()
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