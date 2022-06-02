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

    private val args: CookingCalendarFragmentArgs by navArgs()

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
        val adapter = CalendarCellAdapter(dateList) { item, pos ->
//            Toast.makeText(requireContext(), "Calendar Tap Test", Toast.LENGTH_SHORT).show()
            val selectDate = SimpleDateFormat("yyyy.MM.dd", Locale.JAPAN).format(item.date)
            val action = CookingCalendarFragmentDirections.actionCookingCalendarFragmentToCalendarEditFragment(selectDate, pos)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 7, RecyclerView.VERTICAL, false)

        val pos = args.position
        if (pos >= 0){
            val oldItem = dateList[pos]
            dateList[pos] = CalendarItem(date = oldItem.date, content = args.mainDish)
            adapter.notifyItemChanged(pos)
        }

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