package com.example.recipeapp.viewmodel

import androidx.lifecycle.*
import com.example.recipeapp.adapter.CalendarItem
import com.example.recipeapp.data.Cooking
import com.example.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CalendarViewModel @Inject constructor(private val repository: RecipeRepository) :
    ViewModel() {

    private val myCalendar = Calendar.getInstance(Locale.JAPAN)

    fun addNewCooking(
        date: String,
        main: String,
        side: String,
        memo: String
    ) {
        val newCooking = getNewCooking(date, main, side, memo)
        insertCooking(newCooking)
    }

    private fun getNewCooking(
        date: String,
        main: String,
        side: String,
        memo: String
    ): Cooking {
        return Cooking(date = date, main = main, side = side, memo = memo)
    }

    private fun insertCooking(cooking: Cooking) {
        viewModelScope.launch {
            repository.insertCooking(cooking)
        }
    }


    fun getCooking(date:String) : LiveData<Cooking>{
        return repository.getCooking(date).asLiveData()
    }



    private var currentDate = MutableLiveData(myCalendar.time)

    private var roomData: LiveData<List<Cooking>> = Transformations.switchMap(currentDate) {
        val date = SimpleDateFormat("yyyy.MM", Locale.JAPAN).format(it)
        repository.getCookingList(date).asLiveData()
    }

    val dataList: LiveData<ArrayList<CalendarItem>> = Transformations.map(roomData) { list ->
        getDays(list)
    }

    private fun getDays(roomList: List<Cooking>): ArrayList<CalendarItem> {
        val startDate = myCalendar.time
        val cnt: Int = myCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH) * 7

        myCalendar.set(Calendar.DATE, 1)
        val dayOfWeek: Int = myCalendar.get(Calendar.DAY_OF_WEEK) - 1
        myCalendar.add(Calendar.DATE, -dayOfWeek)

        val days: ArrayList<CalendarItem> = arrayListOf()

        if (roomList.isNotEmpty()) {
            var index = 0
            for (i in 1..cnt) {
                var content = ""
                val date = SimpleDateFormat("yyyy.MM.dd", Locale.JAPAN).format(myCalendar.time)
                if (index < roomList.size && roomList[index].date == date) {
                    content = roomList[index].main
                    index++
                }
                days.add(CalendarItem(date = myCalendar.time, content = content))
                myCalendar.add(Calendar.DATE, 1)
            }
        } else {
            for (i in 1..cnt) {
                days.add(CalendarItem(date = myCalendar.time))
                myCalendar.add(Calendar.DATE, 1)
            }
        }

            myCalendar.time = startDate

            return days
        }


    fun nextMonth() {
        myCalendar.add(Calendar.MONTH, 1)
        currentDate.value = myCalendar.time
        }

    fun prevMonth() {
        myCalendar.add(Calendar.MONTH, -1)
        currentDate.value = myCalendar.time
    }

    fun getTitle(): String {
        return SimpleDateFormat("yyyy.MM", Locale.JAPAN).format(myCalendar.time)
    }
}
