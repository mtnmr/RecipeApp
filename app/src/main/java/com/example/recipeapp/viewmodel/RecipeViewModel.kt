package com.example.recipeapp.viewmodel

import androidx.lifecycle.*
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.repository.RecipeRepository
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

    fun getRecipe(id:Int): LiveData<Recipe>{
          return repository.getRecipe(id).asLiveData()
    }

    fun addNewRecipe(category : Int,
                     title : String,
                     image : String?,
                     ingredients : String?,
                     link : String?,
                     date : String?){
        val newRecipe = getNewRecipe(category, title, image, ingredients, link, date)
        insert(newRecipe)
    }

    private fun getNewRecipe(category : Int,
                             title : String,
                             image : String?,
                             ingredients : String?,
                             link : String?,
                             date : String?) : Recipe {
        return Recipe(
            category = category,
            title = title,
            image = image,
            ingredients = ingredients,
            link = link,
            date = date
        )
    }

    private fun insert(recipe:Recipe){
        viewModelScope.launch {
            repository.insert(recipe)
        }
    }

    fun delete(recipe: Recipe){
        viewModelScope.launch {
            repository.delete(recipe)
        }
    }

    fun updateRecipe(id: Int,
                     category : Int,
                     title : String,
                     image : String?,
                     ingredients : String?,
                     link : String?,
                     date : String?) {
        val updatedRecipe = getUpdateRecipe(id, category, title, image, ingredients, link, date)
        update(updatedRecipe)
    }

    private fun getUpdateRecipe(id: Int,
                                category : Int,
                                title : String,
                                image : String?,
                                ingredients : String?,
                                link : String?,
                                date : String?) : Recipe{
        return Recipe(
            id = id,
            category = category,
            title = title, image,
            ingredients = ingredients,
            link = link,
            date = date)
    }

    private fun update(recipe: Recipe){
        viewModelScope.launch {
            repository.update(recipe)
        }
    }
}


class RecipeViewModelFactory(private val repository: RecipeRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


