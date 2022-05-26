package com.example.recipeapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapter.ListDetailAdapter.ListDetailViewHolder
import com.example.recipeapp.data.ListDetail
import com.example.recipeapp.data.ShoppingList
import com.example.recipeapp.databinding.ListDetailItemBinding

class ListDetailAdapter(private val onItemClicked: (ListDetail) -> Unit) :
    ListAdapter<ListDetail, ListDetailViewHolder>(
        diffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListDetailViewHolder {
        return ListDetailViewHolder(
            ListDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ListDetailViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(item)
        }
        holder.bind(item)
    }


    class ListDetailViewHolder(private val binding: ListDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun bind(detail: ListDetail) {
            binding.detailItemName.text = detail.detailName
            if(detail.checked){
                binding.checkLine.visibility = View.VISIBLE
                binding.listDetailView.setBackgroundColor(R.color.checked_backGround)
            }else{
                binding.checkLine.visibility =View.INVISIBLE
                binding.listDetailView.setBackgroundColor(Color.WHITE)
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ListDetail>() {
            override fun areContentsTheSame(oldItem: ListDetail, newItem: ListDetail): Boolean {
                return oldItem.detailId == newItem.detailId
            }

            override fun areItemsTheSame(oldItem: ListDetail, newItem: ListDetail): Boolean {
                return oldItem === newItem
            }
        }
    }
}