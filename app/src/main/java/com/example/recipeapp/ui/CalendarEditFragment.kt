package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.recipeapp.R
import com.example.recipeapp.data.Cooking
import com.example.recipeapp.databinding.FragmentCalendarEditBinding
import com.example.recipeapp.databinding.FragmentCookingCalendarBinding
import com.example.recipeapp.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarEditFragment : Fragment() {

    private var _binding: FragmentCalendarEditBinding? = null
    private val binding get() = _binding!!

    private val args: CalendarEditFragmentArgs by navArgs()

    private val viewModel: CalendarViewModel by activityViewModels()

    private lateinit var cooking: Cooking

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedDate = args.selectDate
        binding.cookDate.text = selectedDate
        binding.mainDishEdit.setText(args.mainDish)

        viewModel.getCooking(selectedDate).observe(viewLifecycleOwner) {
            if (it != null) {
                cooking = it
                bind(cooking)
            } else {
                binding.cookingSaveButton.setOnClickListener {
                    addNewCooking()
                }
            }
        }

        binding.cookingDeleteButton.setOnClickListener {
            val action =
                CalendarEditFragmentDirections.actionCalendarEditFragmentToCookingCalendarFragment()
            findNavController().navigate(action)
        }
    }

    private fun bind(cooking: Cooking) {
        binding.sideDishEdit.setText(cooking.side)
        binding.cookMemoEdit.setText(cooking.memo)
        binding.cookingDeleteButton.visibility = View.VISIBLE

        binding.cookingDeleteButton.setOnClickListener {
            viewModel.deleteCooking(cooking)
            val action =
                CalendarEditFragmentDirections.actionCalendarEditFragmentToCookingCalendarFragment()
            findNavController().navigate(action)
        }

        binding.cookingSaveButton.setOnClickListener {
            updateCooking()
        }

    }

    private fun updateCooking() {
        if (binding.mainDishEdit.text.toString().isNotEmpty()) {
            viewModel.updateCooking(
                id = cooking.id,
                date = args.selectDate,
                main = binding.mainDishEdit.text.toString(),
                side = binding.sideDishEdit.text.toString(),
                memo = binding.cookMemoEdit.text.toString()
            )
            val action =
                CalendarEditFragmentDirections.actionCalendarEditFragmentToCookingCalendarFragment()
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "メイン料理を入力してください", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addNewCooking() {
        if (binding.mainDishEdit.text.toString().isNotEmpty()) {
            viewModel.addNewCooking(
                date = args.selectDate,
                main = binding.mainDishEdit.text.toString(),
                side = binding.sideDishEdit.text.toString(),
                memo = binding.cookMemoEdit.text.toString()
            )
            val action =
                CalendarEditFragmentDirections.actionCalendarEditFragmentToCookingCalendarFragment()
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "メイン料理を入力してください", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}