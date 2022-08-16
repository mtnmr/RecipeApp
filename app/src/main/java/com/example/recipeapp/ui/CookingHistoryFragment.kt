package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.example.recipeapp.adapter.CookingHistoryAdapter
import com.example.recipeapp.data.Cooking
import com.example.recipeapp.data.datastore.SortSettingsDataStore
import com.example.recipeapp.databinding.FragmentCookingHistoryBinding
import com.example.recipeapp.viewmodel.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CookingHistoryFragment : Fragment() {

    private var _binding: FragmentCookingHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CalendarViewModel by activityViewModels()

    private var sortedASD = true
    private lateinit var settingsDataStore: SortSettingsDataStore

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

        settingsDataStore = SortSettingsDataStore(requireContext())
        settingsDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner){
            sortedASD = it
            sortButtonImageChange()
            viewModel.changeWord(binding.searchHistory.query.toString())
        }

        viewModel.history.observe(viewLifecycleOwner) { items ->
            items.let {
                if(sortedASD){
                    adapter.submitList(it)
                }
                else{
                    adapter.submitList(it.reversed())
                }
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

        binding.sortButton.setOnClickListener {
            sortedASD = !sortedASD
            sortButtonImageChange()
            lifecycleScope.launch {
                settingsDataStore.saveSortToPreferencesStore(sortedASD, requireContext())
            }
            viewModel.changeWord(binding.searchHistory.query.toString())
        }
    }

    private fun sortButtonImageChange(){
        binding.sortButton.rotationX = if(sortedASD) 180F else 0F
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}