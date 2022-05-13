package com.example.recipeapp.repository

import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.RecipeDao
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {

    val appRecipes : Flow<List<Recipe>> = recipeDao.getAllRecipes()

    fun getAllRecipes() : Flow<List<Recipe>>{
        return recipeDao.getAllRecipes()
    }

    fun getRecipe(id : Int): Flow<Recipe>{
        return recipeDao.getRecipe(id)
    }

    fun getSearchRecipes(word : String) : Flow<List<Recipe>>{
        return recipeDao.getSearchRecipes(word)
    }

    fun getCategoryRecipe(num : Int) : Flow<List<Recipe>>{
        return recipeDao.getCategoryRecipe(num)
    }

    suspend fun insert(recipe: Recipe){
        recipeDao.insert(recipe)
    }

    suspend fun delete(recipe: Recipe){
        recipeDao.delete(recipe)
    }

    suspend fun update(recipe: Recipe){
        recipeDao.update(recipe)
    }


}