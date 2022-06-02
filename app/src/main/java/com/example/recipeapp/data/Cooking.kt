package com.example.recipeapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cooking_menu")
data class Cooking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String = "",
    val main: String = "",
    val side: String = "",
    val memo: String = ""
)