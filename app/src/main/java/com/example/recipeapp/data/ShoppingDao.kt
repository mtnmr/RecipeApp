package com.example.recipeapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ShoppingList)

    @Delete
    suspend fun deleteList(list: ShoppingList)

    @Update
    suspend fun updateList(list: ShoppingList)

    @Query("SELECT * FROM shoppinglist")
    fun getAllShoppingList(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shoppinglist WHERE listId = :id")
    fun getShoppingList(id: Int): Flow<ShoppingList>

    @Query("SELECT * FROM shoppinglist ORDER BY listId DESC LIMIT 1")
    fun getCurrentShoppingList() : Flow<ShoppingList>


    //detail
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetail(detail: ListDetail)

    @Delete
    suspend fun deleteDetail(detail: ListDetail)

    @Update
    suspend fun updateDetail(detail: ListDetail)

    @Query("SELECT * FROM listdetail WHERE parentId = :id")
    fun getListDetails(id: Int): Flow<List<ListDetail>>

    @Query("DELETE FROM listdetail WHERE parentId = :id")
    suspend fun deleteListDetails(id: Int)

    //    @Transaction
//    @Query("SELECT * FROM shoppinglist")
//    fun getShoppingListAndDetails(): Flow<List<ShoppingListWithDetails>>
}