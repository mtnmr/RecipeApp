package com.example.recipeapp.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val category : String,
    val title : String,
    val image : String?,
    val ingredients : String?,
    val link : String?,
    val date : String?
)