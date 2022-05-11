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
}