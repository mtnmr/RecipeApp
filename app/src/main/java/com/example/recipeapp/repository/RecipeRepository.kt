package com.example.recipeapp.repository

import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.RecipeDao
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {

    val appRecipes : Flow<List<Recipe>> = recipeDao.getAllRecipes()

    fun getRecipe(id : Int): Flow<Recipe>{
        return recipeDao.getRecipe(id)
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