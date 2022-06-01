package com.example.recipeapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapter.CalendarCellAdapter.ItemViewHolder
import java.text.SimpleDateFormat
import java.util.*

data class CalendarItem(
    val date: Date,
    val content: String? = "sample"
)

class CalendarCellAdapter( var dateList: List<CalendarItem>) : RecyclerView.Adapter<ItemViewHolder>(){

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val dayText: TextView = itemView.findViewById(R.id.day)
        val contentText : TextView = itemView.findViewById(R.id.content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calendar_cell, parent, false)
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
    }

    override fun getItemCount(): Int {
        return dateList.size
    }
}