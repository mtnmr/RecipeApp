package com.example.recipeapp.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: Int,
    val title: String,
    val image: String?,
    val ingredients: String?,
    val link: String?,
    val date: String?,
    val memo:String?,
    val isFavorite: Boolean = false
)