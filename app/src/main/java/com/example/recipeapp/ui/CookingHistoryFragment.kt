package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.recipeapp.R
import com.example.recipeapp.adapter.CookingHistoryAdapter
import com.example.recipeapp.databinding.FragmentCookingCalendarBinding
import com.example.recipeapp.databinding.FragmentCookingHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CookingHistoryFragment : Fragment() {

    private var _binding: FragmentCookingHistoryBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCookingHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CookingHistoryAdapter()
        binding.historyRecyclerview.adapter = adapter
        binding.historyRecyclerview.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}