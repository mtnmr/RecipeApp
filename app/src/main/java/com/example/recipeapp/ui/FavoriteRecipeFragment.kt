package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.recipeapp.R
import com.example.recipeapp.adapter.RecipeListAdapter
import com.example.recipeapp.databinding.FragmentFavoriteRecipeBinding
import com.example.recipeapp.viewmodel.SearchRecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipeFragment : Fragment() {

    private val viewModel: SearchRecipeViewModel by activityViewModels()

    private var _binding: FragmentFavoriteRecipeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RecipeListAdapter {
            val action =FavoriteRecipeFragmentDirections.actionFavoriteRecipeFragmentToRecipeDetailFragment(it.id)
            findNavController().navigate(action)
        }

        binding.favoriteRecipeRecyclerview.adapter = adapter
        binding.favoriteRecipeRecyclerview.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        viewModel.favoriteRecipes.observe(viewLifecycleOwner){items ->
            items.let {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}