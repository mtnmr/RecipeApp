package com.example.recipeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.adapter.CookingHistoryAdapter.CookingHistoryViewHolder
import com.example.recipeapp.data.Cooking
import com.example.recipeapp.databinding.CookingHistoryItemBinding


class CookingHistoryAdapter: ListAdapter<Cooking, CookingHistoryViewHolder>(diffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookingHistoryViewHolder {
        return CookingHistoryViewHolder(
            CookingHistoryItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CookingHistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class CookingHistoryViewHolder(private val binding:CookingHistoryItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(cooking:Cooking){
            binding.date.text = cooking.date
            binding.mainDish.text = cooking.main
            binding.sideDish.text = cooking.side
            binding.memo.text = cooking.memo
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Cooking>() {
            override fun areContentsTheSame(oldItem: Cooking, newItem: Cooking): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(oldItem: Cooking, newItem: Cooking): Boolean {
                return oldItem === newItem
            }
        }
    }
}
