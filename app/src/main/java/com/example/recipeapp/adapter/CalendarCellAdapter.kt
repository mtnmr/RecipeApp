package com.example.recipeapp.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapter.CalendarCellAdapter.ItemViewHolder
import com.example.recipeapp.data.Recipe
import java.text.SimpleDateFormat
import java.util.*

data class CalendarItem(
    val date: Date,
    val content: String = ""
)

class CalendarCellAdapter(
    var dateList: List<CalendarItem>,
    private val onItemClicked: (CalendarItem, Int, String) -> Unit
) : RecyclerView.Adapter<ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.day)
        val contentText: TextView = itemView.findViewById(R.id.content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dateList[position]
        holder.dayText.text = SimpleDateFormat("d", Locale.JAPAN).format(item.date)
        holder.contentText.text = item.content
        when {
            position % 7 == 0 -> {
                holder.dayText.setTextColor(Color.RED)
            }
            position % 7 == 6 -> {
                holder.dayText.setTextColor(Color.BLUE)
            }
            else -> {
                holder.dayText.setTextColor(Color.BLACK)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClicked(item, position, item.content)
        }
    }

    override fun getItemCount(): Int {
        return dateList.size
    }
}