package com.example.recipeapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.ShoppingList
import com.example.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class ShoppingViewModel(private val repository: RecipeRepository) : ViewModel() {

    fun addNewShoppingList(listId:Int, listTitle:String){
        val newList = getNewShoppingList(listId, listTitle)
        insertList(newList)
    }

    private fun getNewShoppingList(listId:Int, listTitle:String): ShoppingList{
        return ShoppingList(listId, listTitle)
    }

    private fun insertList(list: ShoppingList){
        viewModelScope.launch {
            repository.insertList(list)
        }
    }


    private fun deleteList(list: ShoppingList){
        viewModelScope.launch {
            repository.deleteList(list)
        }
    }

    fun updateShoppingList(listId:Int, listTitle:String){
        val updatedList = getNewShoppingList(listId, listTitle)
        updateList(updatedList)
    }

    private fun updateList(list: ShoppingList){
        viewModelScope.launch {
            repository.updateList(list)
        }
    }
}


class ShoppingViewModelFactory(private val repository: RecipeRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ShoppingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}