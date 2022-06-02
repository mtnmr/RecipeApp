package com.example.recipeapp.data

import androidx.room.*

@Dao
interface CookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCooking(cooking: Cooking)

    @Delete
    suspend fun deleteCooking(cooking: Cooking)

    @Update
    suspend fun updateCooking(cooking: Cooking)

    @Query("SELECT * From cooking_menu WHERE date = :date LIMIT 1")
    suspend fun getCooking(date:String) : Cooking
}