package com.example.recipeapp.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.hardware.input.InputManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRecipeEditBinding
import java.util.*

class RecipeEditFragment : Fragment() {

    private var _binding:FragmentRecipeEditBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.recipe_category_list,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = binding.categorySpinner
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val spinner = parent as Spinner
                val str = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.recipeDateEdit.setOnClickListener { showDatePicker(binding.recipeDateEdit) }

    }

    private fun showDatePicker(editText: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                editText.setText(String.format("%d/%02d/%02d", year, month+1, day))
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}