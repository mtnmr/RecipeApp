package com.example.recipeapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface CookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCooking(cooking: Cooking)

    @Delete
    suspend fun deleteCooking(cooking: Cooking)

    @Update
    suspend fun updateCooking(cooking: Cooking)

    @Query("SELECT * From cooking_menu WHERE date = :date LIMIT 1")
    fun getCooking(date: String): Flow<Cooking>

    @Query("SELECT * From cooking_menu WHERE date LIKE :date || '%'")
    fun getCookingList(date: String): Flow<List<Cooking>>
}