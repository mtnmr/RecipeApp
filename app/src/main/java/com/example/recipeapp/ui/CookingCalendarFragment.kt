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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.adapter.CalendarCellAdapter
import com.example.recipeapp.adapter.CalendarItem
import com.example.recipeapp.databinding.FragmentCookingCalendarBinding
import com.example.recipeapp.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class CookingCalendarFragment : Fragment() {

    private var _binding: FragmentCookingCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by activityViewModels()

    private var dateList = listOf<CalendarItem>()

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

        val adapter = CalendarCellAdapter(dateList) { item, pos, content ->
            val selectDate = SimpleDateFormat("yyyy.MM.dd", Locale.JAPAN).format(item.date)
            val action =
                CookingCalendarFragmentDirections.actionCookingCalendarFragmentToCalendarEditFragment(
                    selectDate = selectDate,
                    mainDish = content
                )
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 7, RecyclerView.VERTICAL, false)


        viewModel.dataList.observe(viewLifecycleOwner){
            adapter.dateList = it
            adapter.notifyDataSetChanged()
        }

        binding.titleText.text = viewModel.getTitle()

        binding.prevButton.setOnClickListener {
            viewModel.prevMonth()
            binding.titleText.text = viewModel.getTitle()
        }

        binding.nextButton.setOnClickListener {
            viewModel.nextMonth()
            binding.titleText.text = viewModel.getTitle()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}