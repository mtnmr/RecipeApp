package com.example.recipeapp.viewmodel

import androidx.lifecycle.*
import com.example.recipeapp.adapter.CalendarItem
import com.example.recipeapp.data.Cooking
import com.example.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class CalendarViewModel @Inject constructor(private val repository: RecipeRepository) :
    ViewModel() {

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
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCooking(cooking)
        }
    }

    fun updateCooking(
        id: Int,
        date: String,
        main: String,
        side: String,
        memo: String
    ) {
        val updatedCooking = getUpdatedCooking(id, date, main, side, memo)
        updateCooking(updatedCooking)
    }

    private fun getUpdatedCooking(
        id: Int,
        date: String,
        main: String,
        side: String,
        memo: String
    ): Cooking {
        return Cooking(id, date, main, side, memo)
    }

    private fun updateCooking(cooking: Cooking) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateCooking(cooking)
        }
    }


    fun getCooking(date: String): LiveData<Cooking> {
        return repository.getCooking(date).asLiveData()
    }

    fun deleteCooking(cooking: Cooking) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCooking(cooking)
        }
    }

    private val myCalendar = Calendar.getInstance(Locale.JAPAN)

    private val _currentDate = MutableLiveData(myCalendar.time)
    val currentDate: LiveData<Date> = _currentDate

//    private val roomData: LiveData<List<Cooking>> = Transformations.switchMap(currentDate) {
//        val date = SimpleDateFormat("yyyy.MM", Locale.JAPAN).format(it)
//        repository.getCookingList(date).asLiveData()
//    }
//

    private val format = SimpleDateFormat("yyyy.MM.dd", Locale.JAPAN)

    private val roomData: LiveData<List<Cooking>> = Transformations.switchMap(currentDate) {

        val startDate = myCalendar.time
        val cnt: Int = myCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH) * 7
        myCalendar.set(Calendar.DATE, 1)
        val dayOfWeek: Int = myCalendar.get(Calendar.DAY_OF_WEEK) - 1
        myCalendar.add(Calendar.DATE, -dayOfWeek)

        val start = myCalendar.time
        myCalendar.add(Calendar.DATE, cnt)
        val end = myCalendar.time

        myCalendar.time = startDate

        repository.getNewCookingList(format.format(start), format.format(end)).asLiveData()
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
                val date = format.format(myCalendar.time)
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
        _currentDate.value = myCalendar.time
    }

    fun prevMonth() {
        myCalendar.add(Calendar.MONTH, -1)
        _currentDate.value = myCalendar.time
    }

    //カレンダーの表示月を画面遷移から戻ってくるたびに今月に戻すなら使う
    fun currentMonth() {
        myCalendar.time = Calendar.getInstance(Locale.JAPAN).time
        _currentDate.value = myCalendar.time
    }

    private var _word = MutableLiveData("")
    val word: LiveData<String> = _word

    fun changeWord(text: String) {
        _word.value = text
    }

    val history = Transformations.switchMap(word) { text ->
        repository.getHistoryASC(text).asLiveData()
    }

    val historyDESC = Transformations.switchMap(word) { text ->
        repository.getHistoryDESC(text).asLiveData()
    }
}
