package com.example.recipeapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapter.ListDetailAdapter.ListDetailViewHolder
import com.example.recipeapp.data.ListDetail
import com.example.recipeapp.data.ShoppingList
import com.example.recipeapp.databinding.ListDetailItemBinding

class ListDetailAdapter(
    private val onItemClicked: (ListDetail) -> Unit
) :
    ListAdapter<ListDetail, ListDetailViewHolder>(
        diffCallback
    ) {

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)            // Stable ID利用の宣言
    }

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

        holder.checkBox.setOnClickListener {
            onItemClicked(item)
        }

        holder.bind(item)

        tracker?.let {
            holder.itemView.isActivated = it.isSelected(getItemId(position))
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).detailId.toLong()
    }


    class ListDetailViewHolder(private val binding: ListDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val checkBox = binding.checkbox

        @SuppressLint("ResourceAsColor")
        fun bind(detail: ListDetail) {
            binding.detailItemName.text = detail.detailName

            binding.detailItemName.apply {
                if (detail.checked) {
                    setTextColor(Color.GRAY)
                    paintFlags = binding.detailItemName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    paint.isAntiAlias = true
                    binding.checkbox.isChecked = true
                } else {
                    setTextColor(Color.BLACK)
                    paintFlags = 0
                    paint.isAntiAlias = true
                    binding.checkbox.isChecked = false
                }
            }

//            if(detail.checked){
//                binding.checkLine.visibility = View.VISIBLE
//                binding.listDetailView.setBackgroundColor(R.color.checked_backGround)
//            }else{
//                binding.checkLine.visibility =View.INVISIBLE
//                binding.listDetailView.setBackgroundColor(Color.WHITE)
//            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ListDetail>() {
            override fun areContentsTheSame(oldItem: ListDetail, newItem: ListDetail): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: ListDetail, newItem: ListDetail): Boolean {
                return oldItem.detailId == newItem.detailId
            }
        }
    }
}