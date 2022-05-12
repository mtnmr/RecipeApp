package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.MyApplication
import com.example.recipeapp.R
import com.example.recipeapp.adapter.RecipeListAdapter
import com.example.recipeapp.databinding.FragmentHomeBinding
import com.example.recipeapp.viewmodel.RecipeViewModel
import com.example.recipeapp.viewmodel.RecipeViewModelFactory

class HomeFragment : Fragment() {

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory((activity?.application as MyApplication).repository)
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


        val adapter = RecipeListAdapter{
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(it.id)
            findNavController().navigate(action)
        }

        binding.recipeRecyclerview.adapter = adapter

        viewModel.allRecipes.observe(this.viewLifecycleOwner){ items ->
            items.let {
                adapter.submitList(it)
            }
        }

//        var word : String = "あ"
//
//        binding.searchButton.setOnClickListener {
//            word = binding.searchRecipe.text.toString()
//        }
//
//        viewModel.getSearchRecipe(word).observe(this.viewLifecycleOwner){ items ->
//            items.let {
//                adapter.submitList(it)
//            }
//        }


        binding.addRecipeButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeEditFragment()
            findNavController().navigate(action)
        }

//Roomテスト用
//        viewModel.allRecipes.observe(this.viewLifecycleOwner){
//            binding.roomTest.text = it.toString()
//        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}