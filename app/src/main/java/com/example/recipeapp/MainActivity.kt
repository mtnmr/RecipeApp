package com.example.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recipeapp.databinding.FragmentRecipeEditBinding
import com.example.recipeapp.ui.RecipeEditFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = RecipeEditFragment()
        fragmentTransaction.add(R.id.container, fragment)
        fragmentTransaction.commit()
    }
}