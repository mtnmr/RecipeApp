package com.example.recipeapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.recipeapp.adapter.CalendarItem
import com.example.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CalendarViewModel @Inject constructor(private val repository: RecipeRepository) :
    ViewModel() {

    private val myCalendar = Calendar.getInstance(Locale.JAPAN)

    fun getDays(): ArrayList<CalendarItem> {
        val startDate = myCalendar.time
        val cnt: Int = myCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH) * 7

        myCalendar.set(Calendar.DATE, 1)
        val datOfWeek: Int = myCalendar.get(Calendar.DAY_OF_WEEK) - 1
        myCalendar.add(Calendar.DATE, -datOfWeek)

        val days: ArrayList<CalendarItem> = arrayListOf()

        for (i in 1..cnt) {
            days.add(CalendarItem(date = myCalendar.time))
            myCalendar.add(Calendar.DATE, 1)
        }

        myCalendar.time = startDate

        return days
    }


    fun getTitle(): String {
        return SimpleDateFormat("yyyy.MM", Locale.JAPAN).format(myCalendar.time)
    }

    fun nextMonth(): List<CalendarItem> {
        myCalendar.add(Calendar.MONTH, 1)
        return getDays()
    }

    fun prevMonth(): List<CalendarItem> {
        myCalendar.add(Calendar.MONTH, -1)
        return getDays()
    }

}