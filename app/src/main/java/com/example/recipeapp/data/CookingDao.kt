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

    @Query("SELECT * From cooking_menu WHERE date LIKE :date || '%' ORDER BY date ASC")
    fun getCookingList(date: String): Flow<List<Cooking>>

    @Query("SELECT * From cooking_menu WHERE (main LIKE '%' || :word || '%' OR side LIKE '%' || :word || '%') ORDER BY date ASC")
    fun getHistoryASC(word:String) : Flow<List<Cooking>>

    @Query("SELECT * From cooking_menu WHERE (main LIKE '%' || :word || '%' OR side LIKE '%' || :word || '%') ORDER BY date DESC")
    fun getHistoryDESC(word:String) : Flow<List<Cooking>>

    @Query("SELECT * From cooking_menu WHERE date BETWEEN :start AND :end ORDER BY date ASC")
    fun getNewCookingList(start:String, end:String): Flow<List<Cooking>>
}