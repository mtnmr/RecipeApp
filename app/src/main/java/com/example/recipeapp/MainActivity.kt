package com.example.recipeapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
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

    //背景タップでキーボード非表示
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//
//        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        val layout = findViewById<ConstraintLayout>(R.id.constraint_layout)
//        inputManager.hideSoftInputFromWindow(
//            layout.windowToken,
//            InputMethodManager.HIDE_NOT_ALWAYS
//        )
//
//        return false
//    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val layout = findViewById<ConstraintLayout>(R.id.constraint_layout)
        inputManager.hideSoftInputFromWindow(
            layout.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

        return super.dispatchTouchEvent(ev)
    }
}