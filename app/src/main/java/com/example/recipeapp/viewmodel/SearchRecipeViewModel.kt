package com.example.recipeapp.viewmodel

import androidx.lifecycle.*
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.repository.RecipeRepository

class SearchRecipeViewModel(private val repository: RecipeRepository) : ViewModel() {

    val allRecipes : LiveData<List<Recipe>> = repository.appRecipes.asLiveData()

    private fun getAllRecipe(): LiveData<List<Recipe>> {
        return repository.getAllRecipes().asLiveData()
    }

    private var _word = MutableLiveData("")
    val word : LiveData<String> = _word

    fun changeWord(text : String){
        _word.value = text
    }

    val searchRecipes = Transformations.switchMap(word){ text ->
        getSearchRecipe(text)
    }

    private fun getSearchRecipe(word: String): LiveData<List<Recipe>> {
        return repository.getSearchRecipes(word).asLiveData()
    }

    private val _categoryNum = MutableLiveData(0)
    val categoryNum : LiveData<Int> = _categoryNum

    val categoryRecipe = Transformations.switchMap(categoryNum){ num ->
        getCategoryRecipe(num)
    }

    fun changeNum(num : Int){
        _categoryNum.value = num
    }

    private fun getCategoryRecipe(num : Int) : LiveData<List<Recipe>>{
        return if (num >= 0) {
            repository.getCategoryRecipe(num).asLiveData()
        }else{
            getAllRecipe()
        }
    }
}

class SearchRecipeViewModelFactory(private val repository: RecipeRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchRecipeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SearchRecipeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}