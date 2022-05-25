package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.recipeapp.MyApplication
import com.example.recipeapp.R
import com.example.recipeapp.adapter.RecipeListAdapter
import com.example.recipeapp.databinding.FragmentHomeBinding
import com.example.recipeapp.viewmodel.RecipeViewModel
import com.example.recipeapp.viewmodel.RecipeViewModelFactory
import com.example.recipeapp.viewmodel.SearchRecipeViewModel
import com.example.recipeapp.viewmodel.SearchRecipeViewModelFactory

class HomeFragment : Fragment() {

    private val viewModel: SearchRecipeViewModel by activityViewModels {
        SearchRecipeViewModelFactory((activity?.application as MyApplication).repository)
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = RecipeListAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(it.id)
            findNavController().navigate(action)
        }

        binding.recipeRecyclerview.adapter = adapter
        binding.recipeRecyclerview.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

//        viewModel.allRecipes.observe(this.viewLifecycleOwner){ items ->
//            items.let {
//                adapter.submitList(it)
//            }
//        }

//        viewModel.searchRecipes.observe(this.viewLifecycleOwner) { items ->
//            items.let {
//                adapter.submitList(it)
//            }
//        }
//
//        viewModel.categoryRecipe.observe(this.viewLifecycleOwner) { items ->
//            items.let {
//                adapter.submitList(it)
//            }
//        }

        viewModel.recipes.observe(this.viewLifecycleOwner){items ->
            items.let {
                adapter.submitList(it)
            }
        }

//        binding.searchButton.setOnClickListener {
//            val word = binding.searchRecipe.text.toString()
//            viewModel.changeWord(word)
//        }


        binding.searchRecipe.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.changeWord(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.changeWord(query)
                return false
            }
        })

        viewModel.categoryNum.observe(this.viewLifecycleOwner){
            binding.categoryGroup.check(
                when(it){
                    -1 -> R.id.category_all
                    0 -> R.id.category_main
                    1 -> R.id.category_sub
                    2 -> R.id.category_rice
                    else -> R.id.category_dessert
                }
            )
        }

        binding.categoryGroup.setOnCheckedChangeListener { view, id ->
            var num = -1
            when (id) {
                R.id.category_all -> num = -1
                R.id.category_main -> num = 0
                R.id.category_sub -> num = 1
                R.id.category_rice -> num = 2
                R.id.category_dessert -> num = 3
            }
            viewModel.changeNum(num)
        }


        binding.addRecipeButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeEditFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}