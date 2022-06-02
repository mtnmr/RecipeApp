package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentCalendarEditBinding
import com.example.recipeapp.databinding.FragmentCookingCalendarBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarEditFragment : Fragment() {

    private var _binding: FragmentCalendarEditBinding? = null
    private val binding get() = _binding!!

    private val args: CalendarEditFragmentArgs by navArgs()

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

        binding.cookingDeleteButton.setOnClickListener {
            val action =
                CalendarEditFragmentDirections.actionCalendarEditFragmentToCookingCalendarFragment(
                    position = args.position
                )
            findNavController().navigate(action)
        }

        binding.cookingSaveButton.setOnClickListener {
            val action =
                CalendarEditFragmentDirections.actionCalendarEditFragmentToCookingCalendarFragment(
                    mainDish = "main",
                    position = args.position
                )
            findNavController().navigate(action)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}