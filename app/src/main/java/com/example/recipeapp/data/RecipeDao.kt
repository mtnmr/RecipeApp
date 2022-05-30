package com.example.recipeapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes() : Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    fun getRecipe(id : Int) : Flow<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe : Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :word || '%' OR ingredients LIKE '%' || :word || '%'")
    fun getSearchRecipes(word:String): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE category = :num")
    fun getCategoryRecipe(num : Int) :  Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE (title LIKE '%' || :word || '%' OR ingredients LIKE '%' || :word || '%') AND category = :num")
    fun getRecipes(word: String, num: Int) : Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    fun getFavoriteRecipe() : Flow<List<Recipe>>
}