package com.example.recipeapp.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipeapp.data.RecipeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RecipeDatabase::class.java, "recipe_database")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideRecipeDao(db: RecipeDatabase) = db.recipeDao()

    @Singleton
    @Provides
    fun provideShoppingDao(db: RecipeDatabase) = db.shoppingDao()

    @Singleton
    @Provides
    fun provideCookingDao(db : RecipeDatabase) = db.cookingDao()
}