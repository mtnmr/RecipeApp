package com.example.recipeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.adapter.ShoppingListAdapter.ShoppingListViewHolder
import com.example.recipeapp.data.ShoppingList
import com.example.recipeapp.databinding.ShoppingListItemBinding

class ShoppingListAdapter(private val onItemClicked:(ShoppingList) -> Unit):
    ListAdapter<ShoppingList, ShoppingListViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        return ShoppingListViewHolder(
            ShoppingListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
        holder.bind(item)
    }


    class ShoppingListViewHolder(private val binding: ShoppingListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(shoppingList: ShoppingList){
            binding.shoppingList.text = shoppingList.listTitle
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ShoppingList>() {
            override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList): Boolean {
                return oldItem.listId == newItem.listId
            }
        }
    }
}