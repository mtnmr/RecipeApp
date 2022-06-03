package com.example.recipeapp.viewmodel

import androidx.lifecycle.*
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val repository: RecipeRepository) : ViewModel() {

    fun getRecipe(id: Int): LiveData<Recipe> {
        return repository.getRecipe(id).asLiveData()
    }

    fun addNewRecipe(
        category: Int,
        title: String,
        image: String,
        ingredients: String,
        link: String,
        date: String,
        memo:String,
        isFavorite: Boolean
    ) {
        val newRecipe = getNewRecipe(category, title, image, ingredients, link, date, memo, isFavorite)
        insert(newRecipe)
    }

    private fun getNewRecipe(
        category: Int,
        title: String,
        image: String,
        ingredients: String,
        link: String,
        date: String,
        memo: String,
        isFavorite: Boolean
    ): Recipe {
        return Recipe(
            category = category,
            title = title,
            image = image,
            ingredients = ingredients,
            link = link,
            date = date,
            memo = memo,
            isFavorite = isFavorite
        )
    }

    private fun insert(recipe: Recipe) {
        viewModelScope.launch {
            repository.insert(recipe)
        }
    }

    fun delete(recipe: Recipe) {
        viewModelScope.launch {
            repository.delete(recipe)
        }
    }

    fun updateRecipe(
        id: Int,
        category: Int,
        title: String,
        image: String,
        ingredients: String,
        link: String,
        date: String,
        memo: String,
        isFavorite: Boolean
    ) {
        val updatedRecipe =
            getUpdateRecipe(id, category, title, image, ingredients, link, date, memo, isFavorite)
        update(updatedRecipe)
    }

    private fun getUpdateRecipe(
        id: Int,
        category: Int,
        title: String,
        image: String,
        ingredients: String,
        link: String,
        date: String,
        memo: String,
        isFavorite: Boolean
    ): Recipe {
        return Recipe(
            id = id,
            category = category,
            title = title, image,
            ingredients = ingredients,
            link = link,
            date = date,
            memo = memo,
            isFavorite = isFavorite
        )
    }

    private fun update(recipe: Recipe) {
        viewModelScope.launch {
            repository.update(recipe)
        }
    }

    fun updateFavorite(b: Boolean, id: Int) {
        viewModelScope.launch {
            repository.updateFavorite(b, id)
        }
    }
}


//class RecipeViewModelFactory(private val repository: RecipeRepository): ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)){
//            @Suppress("UNCHECKED_CAST")
//            return RecipeViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}


