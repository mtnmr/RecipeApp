package com.example.recipeapp.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class MyItemDetailsLookup(private val recyclerView: RecyclerView)
    : ItemDetailsLookup<Long>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val _view = recyclerView.findChildViewUnder(e.x, e.y)
        return _view?.let {
            val _holder = recyclerView.getChildViewHolder(it)
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = _holder.absoluteAdapterPosition
                override fun getSelectionKey(): Long = _holder.itemId
            }
        }
    }
    }