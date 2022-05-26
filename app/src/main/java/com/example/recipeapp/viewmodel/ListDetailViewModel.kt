package com.example.recipeapp.viewmodel

import androidx.lifecycle.*
import com.example.recipeapp.data.ListDetail
import com.example.recipeapp.data.ShoppingList
import com.example.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class ListDetailViewModel (private val repository: RecipeRepository) : ViewModel() {

    val currentShoppingList:LiveData<ShoppingList> = repository.getCurrentShoppingList().asLiveData()

    fun deleteList(list: ShoppingList) {
        viewModelScope.launch {
            repository.deleteList(list)
        }
    }

    fun updateShoppingList(listId: Int, listTitle: String) {
        val updatedList = getUpdatedShoppingList(listId, listTitle)
        updateList(updatedList)
    }

    private fun getUpdatedShoppingList(listId: Int, listTitle: String): ShoppingList {
        return ShoppingList(listId, listTitle)
    }

    private fun updateList(list: ShoppingList) {
        viewModelScope.launch {
            repository.updateList(list)
        }
    }

    fun getShoppingList(id: Int): LiveData<ShoppingList> {
        return repository.getShoppingList(id).asLiveData()
    }


    //detail
    fun addNewDetail(id: Int, word:String){
        val detail = getNewDetail(id, word)
        insertDetail(detail)
    }

    private fun getNewDetail(id:Int, word:String) : ListDetail {
        return ListDetail(parentId = id, detailName = word, check = false)
    }

    private fun insertDetail(detail: ListDetail){
        viewModelScope.launch {
            repository.insertDetail(detail)
        }
    }

    val currentId = MutableLiveData(0)

    fun changeCurrentId(id:Int){
        currentId.value = id
    }

    val currentListDetails = Transformations.switchMap(currentId){
        repository.getListDetails(it).asLiveData()
    }
}

class ListDetailViewModelFactory(private val repository: RecipeRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}