package com.example.recipeapp.repository

import com.example.recipeapp.data.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao,
    private val shoppingDao: ShoppingDao,
    private val cookingDao: CookingDao
) {

    val appRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes()

    val favoriteRecipes: Flow<List<Recipe>> = recipeDao.getFavoriteRecipe()

    fun getAllRecipes(): Flow<List<Recipe>> {
        return recipeDao.getAllRecipes()
    }

    fun getRecipe(id: Int): Flow<Recipe> {
        return recipeDao.getRecipe(id)
    }

    fun getSearchRecipes(word: String): Flow<List<Recipe>> {
        return recipeDao.getSearchRecipes(word)
    }

    fun getCategoryRecipe(num: Int): Flow<List<Recipe>> {
        return recipeDao.getCategoryRecipe(num)
    }

    fun getRecipes(word: String, num: Int): Flow<List<Recipe>> {
        return recipeDao.getRecipes(word, num)
    }

    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

    suspend fun update(recipe: Recipe) {
        recipeDao.update(recipe)
    }

    suspend fun updateFavorite(b: Boolean, id: Int) {
        recipeDao.updateFavorite(b, id)
    }


    //ShoppingDao
    suspend fun insertList(list: ShoppingList) {
        shoppingDao.insertList(list)
    }

    suspend fun updateList(list: ShoppingList) {
        shoppingDao.updateList(list)
    }

    suspend fun deleteList(list: ShoppingList) {
        shoppingDao.deleteList(list)
    }

    val allShoppingList: Flow<List<ShoppingList>> = shoppingDao.getAllShoppingList()

    fun getShoppingList(id: Int): Flow<ShoppingList> {
        return shoppingDao.getShoppingList(id)
    }

    fun getCurrentShoppingList(): Flow<ShoppingList> {
        return shoppingDao.getCurrentShoppingList()
    }


    suspend fun insertDetail(detail: ListDetail) {
        shoppingDao.insertDetail(detail)
    }

    suspend fun deleteDetail(detail: ListDetail) {
        shoppingDao.deleteDetail(detail)
    }


    fun getListDetails(id: Int): Flow<List<ListDetail>> {
        return shoppingDao.getListDetails(id)
    }

    suspend fun deleteListDetails(id: Int) {
        shoppingDao.deleteListDetails(id)
    }

    suspend fun updateChecked(b: Boolean, id: Int) {
        shoppingDao.updateChecked(b, id)
    }

    suspend fun deleteDetail(id:Int){
        shoppingDao.deleteDetail(id)
    }


    //cooking
    suspend fun insertCooking(cooking: Cooking){
        cookingDao.insertCooking(cooking)
    }

    suspend fun deleteCooking(cooking: Cooking){
        cookingDao.deleteCooking(cooking)
    }

    suspend fun updateCooking(cooking: Cooking){
        cookingDao.updateCooking(cooking)
    }

//    suspend fun getCooking(date:String) : Cooking{
//        return cookingDao.getCooking(date)
//    }

    fun getCookingList(date:String) : Flow<List<Cooking>>{
        return cookingDao.getCookingList(date)
    }
}