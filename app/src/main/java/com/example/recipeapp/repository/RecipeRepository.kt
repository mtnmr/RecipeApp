package com.example.recipeapp.repository

import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.RecipeDao
import com.example.recipeapp.data.ShoppingDao
import com.example.recipeapp.data.ShoppingList
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao, private val shoppingDao: ShoppingDao) {

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

    fun getRecipes(word: String, num: Int) : Flow<List<Recipe>>{
        return recipeDao.getRecipes(word, num)
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

    //ShoppingDao
    suspend fun insertList(list: ShoppingList){
        shoppingDao.insertList(list)
    }

    suspend fun updateList(list: ShoppingList){
        shoppingDao.updateList(list)
    }

    suspend fun deleteList(list: ShoppingList){
        shoppingDao.deleteList(list)
    }

   val allShoppingList: Flow<List<ShoppingList>> = shoppingDao.getAllShoppingList()


}