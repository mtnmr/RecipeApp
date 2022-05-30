package com.example.recipeapp

import android.app.Application
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.data.RecipeDatabase
import com.example.recipeapp.repository.RecipeRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(){
//    val database : RecipeDatabase by lazy {
//        RecipeDatabase.getDatabase(this)
//    }
//
//    val repository  by lazy {
//        RecipeRepository(database.recipeDao(), database.shoppingDao())
//    }
}