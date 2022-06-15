package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import com.example.recipeapp.adapter.CookingHistoryAdapter
import com.example.recipeapp.databinding.FragmentCookingHistoryBinding
import com.example.recipeapp.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CookingHistoryFragment : Fragment() {

    private var _binding: FragmentCookingHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CalendarViewModel by activityViewModels()

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
//        binding.historyRecyclerview.addItemDecoration(
//            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
//        )

        viewModel.history.observe(viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        binding.searchHistory.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.changeWord(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.changeWord(query)
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}