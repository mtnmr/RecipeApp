package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapter.CalendarCellAdapter
import com.example.recipeapp.databinding.FragmentCookingCalendarBinding
import com.example.recipeapp.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CookingCalendarFragment : Fragment() {

    private var _binding : FragmentCookingCalendarBinding ?= null
    private val binding get() = _binding!!

    private val viewModel : CalendarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCookingCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerview

        val dateList = viewModel.getDays()
        val adapter = CalendarCellAdapter(dateList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 7,  RecyclerView.VERTICAL, false)

        binding.titleText.text = viewModel.getTitle()

        binding.prevButton.setOnClickListener {
            adapter.dateList = viewModel.prevMonth()
            adapter.notifyDataSetChanged()
            binding.titleText.text = viewModel.getTitle()
        }

        binding.nextButton.setOnClickListener {
            adapter.dateList = viewModel.nextMonth()
            adapter.notifyDataSetChanged()
            binding.titleText.text = viewModel.getTitle()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}