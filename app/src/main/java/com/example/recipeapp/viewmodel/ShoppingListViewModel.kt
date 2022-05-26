package com.example.recipeapp.viewmodel

import androidx.lifecycle.*
import com.example.recipeapp.data.ListDetail
import com.example.recipeapp.data.ShoppingList
import com.example.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class ShoppingListViewModel(private val repository: RecipeRepository) : ViewModel() {

    val allShoppingList: LiveData<List<ShoppingList>> = repository.allShoppingList.asLiveData()

    fun addNewShoppingList() {
        val newList = ShoppingList()
        insertList(newList)
    }

//    private fun getNewShoppingList(listTitle:String): ShoppingList{
//        return ShoppingList(listTitle = listTitle)
//    }

    private fun insertList(list: ShoppingList) {
        viewModelScope.launch {
            repository.insertList(list)
        }
    }

}


class ShoppingViewModelFactory(private val repository: RecipeRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShoppingListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}