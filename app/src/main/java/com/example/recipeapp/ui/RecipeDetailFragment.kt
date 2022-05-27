package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.recipeapp.MyApplication
import com.example.recipeapp.R
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.databinding.FragmentRecipeDetailBinding
import com.example.recipeapp.viewmodel.RecipeViewModel
import com.example.recipeapp.viewmodel.RecipeViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory((activity?.application as MyApplication).repository)
    }

    private val args: RecipeDetailFragmentArgs by navArgs()

    private lateinit var recipe: Recipe



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.itemId
        viewModel.getRecipe(id).observe(viewLifecycleOwner) { item ->
            recipe = item
            bind(recipe)
        }

        binding.makeListButton.setOnClickListener {
            showListDialog()
        }

    }


    private fun showListDialog(){
        val selectedItems = ArrayList<Int>()
        val items = recipe.ingredients?.split("\n")?.toTypedArray()
        val checkedItems = BooleanArray(items?.size ?: 0)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(recipe.title)
            .setMultiChoiceItems(items, checkedItems) { dialog, which, isChecked ->
                if (isChecked) {
                    selectedItems.add(which)
                } else if (selectedItems.contains(which)) {
                    selectedItems.remove(which)
                }
            }
            .setPositiveButton(R.string.make_button){dialog, which ->
            }
            .setNegativeButton(R.string.cancel_button, null)
            .show()
    }

    private fun bind(recipe: Recipe) {
        binding.apply {
            detailTitle.text = recipe.title
//            Log.d("Recipe", recipe.image.toString())
            if (recipe.image.toString() != "null") {
                detailImage.setImageURI(recipe.image?.toUri())
//                Log.d("Recipe", "uri image set")
            }
            detailIngredients.text = recipe.ingredients.toString()
            detailLink.text = recipe.link.toString()
            detailDate.text = recipe.date.toString()

            editRecipeButton.setOnClickListener {
                val action =
                    RecipeDetailFragmentDirections.actionRecipeDetailFragmentToRecipeEditFragment(
                        recipe.id
                    )
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}