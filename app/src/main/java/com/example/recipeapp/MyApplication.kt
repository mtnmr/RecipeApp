package com.example.recipeapp

import android.app.Application
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.RecipeDatabase

class MyApplication : Application(){
    val database : RecipeDatabase by lazy {
        RecipeDatabase.getDatabase(this)
    }
}