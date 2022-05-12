package com.example.recipeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.adapter.RecipeListAdapter.*
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.databinding.RecipeListItemBinding


class RecipeListAdapter(private val onItemClicked:(Recipe) -> Unit) : ListAdapter<Recipe, RecipeViewHolder>(diffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {

        return RecipeViewHolder(
            RecipeListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
        holder.bind(item)
    }



    class RecipeViewHolder(private val binding:RecipeListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(recipe:Recipe){
            binding.recipeListTitle.text = recipe.title
//            val uri = recipe.image?.toUri()
//            if (uri != null) {
//                binding.recipeListImage.setImageURI(uri)
//            }
        }
    }


    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem === newItem
            }
        }
    }
}






