package com.example.recipeapp.viewmodel

import androidx.lifecycle.*
import com.example.recipeapp.data.ListDetail
import com.example.recipeapp.data.ShoppingList
import com.example.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(private val repository: RecipeRepository) : ViewModel() {

    val allShoppingList: LiveData<List<ShoppingList>> = repository.allShoppingList.asLiveData()

    fun addNewShoppingList() {
        val newList = ShoppingList()
        insertList(newList)
    }

    private fun insertList(list: ShoppingList) {
        viewModelScope.launch {
            repository.insertList(list)
        }
    }

    fun makeNewShoppingList(listTitle: String){
        val newList = getNewShoppingList(listTitle)
        insertList(newList)
    }

    private fun getNewShoppingList(listTitle:String): ShoppingList{
        return ShoppingList(listTitle = listTitle)
    }
}

//class ShoppingViewModelFactory(private val repository: RecipeRepository) :
//    ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ShoppingListViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}