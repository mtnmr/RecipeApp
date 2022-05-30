package com.example.recipeapp.viewmodel

import androidx.lifecycle.*
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchRecipeViewModel @Inject constructor(private val repository: RecipeRepository) :
    ViewModel() {

    val allRecipes: LiveData<List<Recipe>> = repository.appRecipes.asLiveData()

    val favoriteRecipes: LiveData<List<Recipe>> = repository.favoriteRecipes.asLiveData()

    private fun getAllRecipe(): LiveData<List<Recipe>> {
        return repository.getAllRecipes().asLiveData()
    }

    private var _word = MutableLiveData("")
    val word: LiveData<String> = _word

    private val _categoryNum = MutableLiveData(-1)
    val categoryNum: LiveData<Int> = _categoryNum

    fun changeWord(text: String) {
        _word.value = text
    }

    fun changeNum(num: Int) {
        _categoryNum.value = num
    }


    private val searchRecipes = Transformations.switchMap(word) { text ->
        when (categoryNum.value) {
            -1 -> getSearchRecipe(text)
            else -> getRecipes(text, categoryNum.value!!)
        }
    }

    private fun getSearchRecipe(word: String): LiveData<List<Recipe>> {
        return repository.getSearchRecipes(word).asLiveData()
    }

    private val categoryRecipe = Transformations.switchMap(categoryNum) { num ->
        when (num) {
            -1 -> getSearchRecipe(word.value.toString())
            else -> getRecipes(word.value.toString(), num)
        }
    }

    private fun getCategoryRecipe(num: Int): LiveData<List<Recipe>> {
        return if (num >= 0) {
            repository.getCategoryRecipe(num).asLiveData()
        } else {
            getAllRecipe()
        }
    }

    private fun getRecipes(word: String, num: Int): LiveData<List<Recipe>> {
        return repository.getRecipes(word, num).asLiveData()
    }

    val recipes = MediatorLiveData<List<Recipe>>()

    init {
        recipes.addSource(searchRecipes) {
            recipes.value = it
        }

        recipes.addSource(categoryRecipe) {
            recipes.value = it
        }
    }

//    private fun <T1, T2, S> combineLatest(
//        source1: LiveData<T1>, source2: LiveData<T2>,
//        func: (T1?, T2?) -> S?
//    ): LiveData<S> {
//        val result = MediatorLiveData<S>()
//        result.addSource(source1) {
//            result.value = func.invoke(source1.value, source2.value)
//        }
//        result.addSource(source2) {
//            result.value = func.invoke(source1.value, source2.value)
//        }
//        return result
//    }
//
//    val recipes : LiveData<List<Recipe>> = combineLatest(word, categoryNum){ word, num ->
////            getRecipes(word!!, num!!).value
//        allRecipes.value
//    }
}

//class SearchRecipeViewModelFactory(private val repository: RecipeRepository): ViewModelProvider.Factory{
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(SearchRecipeViewModel::class.java)){
//            @Suppress("UNCHECKED_CAST")
//            return SearchRecipeViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}