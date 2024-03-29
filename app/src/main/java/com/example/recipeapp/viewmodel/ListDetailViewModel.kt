package com.example.recipeapp.viewmodel

import androidx.lifecycle.*
import com.example.recipeapp.data.ListDetail
import com.example.recipeapp.data.ShoppingList
import com.example.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListDetailViewModel @Inject constructor(private val repository: RecipeRepository) :
    ViewModel() {

    val currentShoppingList: LiveData<ShoppingList> =
        repository.getCurrentShoppingList().asLiveData()

    fun deleteList(list: ShoppingList) {
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateList(list)
        }
    }

    fun getShoppingList(id: Int): LiveData<ShoppingList> {
        return repository.getShoppingList(id).asLiveData()
    }


    //detail
    fun addNewDetail(id: Int, word: String) {
        val detail = getNewDetail(id, word)
        insertDetail(detail)
    }

    private fun getNewDetail(id: Int, word: String): ListDetail {
        return ListDetail(parentId = id, detailName = word, checked = false)
    }

    private fun insertDetail(detail: ListDetail) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertDetail(detail)
        }
    }

    val currentId = MutableLiveData(0)

    fun changeCurrentId(id: Int) {
        currentId.value = id
    }

    val currentListDetails = Transformations.switchMap(currentId) {
        repository.getListDetails(it).asLiveData()
    }

    fun changeChecked(detail: ListDetail) {
        val b = !detail.checked
        updateChecked(b, detail.detailId)
    }

    private fun updateChecked(b: Boolean, id: Int) {
        viewModelScope.launch(Dispatchers.IO){
            repository.updateChecked(b, id)
        }
    }

    fun deleteListDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteListDetails(id)
        }
    }

//    fun deleteDetail(detail: ListDetail) {
//        viewModelScope.launch {
//            repository.deleteDetail(detail)
//        }
//    }

    fun deleteDetail(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDetail(id)
        }
    }
}

//class ListDetailViewModelFactory(private val repository: RecipeRepository) :
//    ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(ListDetailViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return ListDetailViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}