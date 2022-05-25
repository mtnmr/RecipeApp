package com.example.recipeapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ShoppingList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetail(detail: ListDetail)

    @Delete
    suspend fun deleteList(list: ShoppingList)

    @Delete
    suspend fun deleteDetail(detail: ListDetail)

    @Update
    suspend fun updateList(list: ShoppingList)

    @Update
    suspend fun updateDetail(detail: ListDetail)

    @Transaction
    @Query("SELECT * FROM shoppinglist")
    fun getShoppingListAndDetails(): Flow<List<ShoppingListWithDetails>>

    @Query("SELECT * FROM shoppinglist")
    fun getAllShoppingList(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM listdetail WHERE parentId = :id")
    fun getListDetails(id: Int): Flow<List<ListDetail>>
}