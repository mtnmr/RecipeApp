package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.recipeapp.R
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.databinding.FragmentRecipeDetailBinding
import com.example.recipeapp.viewmodel.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!

    private val recipeViewModel: RecipeViewModel by activityViewModels()


    private val shoppingListViewModel: ShoppingListViewModel by activityViewModels()

    private val detailViewModel: ListDetailViewModel by activityViewModels()


    private val args: RecipeDetailFragmentArgs by navArgs()

    private lateinit var recipe: Recipe

    private var favorite = false

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
        recipeViewModel.getRecipe(id).observe(viewLifecycleOwner) { item ->
            recipe = item
            favorite = recipe.isFavorite
            if (!favorite) {
                binding.favoriteRecipeButton.visibility = View.INVISIBLE
            }
            bind(recipe)
        }

        binding.addFavoriteButton.setOnClickListener {
            recipeViewModel.updateFavorite(true, id)
            binding.addFavoriteButton.visibility = View.INVISIBLE
            binding.favoriteRecipeButton.visibility = View.VISIBLE
        }

        binding.favoriteRecipeButton.setOnClickListener {
            recipeViewModel.updateFavorite(false, id)
            binding.addFavoriteButton.visibility = View.VISIBLE
            binding.favoriteRecipeButton.visibility = View.INVISIBLE
        }

        binding.makeListButton.setOnClickListener {
            showListDialog()
        }

        detailViewModel.currentShoppingList.observe(viewLifecycleOwner) {}

        binding.detailImage.setOnClickListener {
            val dialog = FullScreenDialogFragment()
            dialog.uri = recipe.image.toString()
            dialog.show(parentFragmentManager, "dialog")
        }

    }


    private fun showListDialog() {
        shoppingListViewModel.makeNewShoppingList(recipe.title)
        val selectedItems = ArrayList<Int>()
        val items = recipe.ingredients?.split("\n")?.toTypedArray()
        val checkedItems = BooleanArray(items?.size ?: 0)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(recipe.title)
            .setMultiChoiceItems(items, checkedItems) { _, which, isChecked ->
                if (isChecked) {
                    selectedItems.add(which)
                } else if (selectedItems.contains(which)) {
                    selectedItems.remove(which)
                }
            }
            .setPositiveButton(R.string.make_button) { _, _ ->
                makeList(selectedItems, items)
            }
            .setNegativeButton(R.string.cancel_button) { _, _ ->
                cancelList()
            }
            .setCancelable(false)
            .show()
    }

    private fun makeList(selectedItems: ArrayList<Int>, items: Array<String>?) {
        val listId = detailViewModel.currentShoppingList.value!!.listId
        for (selectedItem in selectedItems) {
            val item = items!![selectedItem]
            detailViewModel.addNewDetail(listId, item)
        }
        Toast.makeText(
            requireContext(),
            getString(R.string.make_list_finish_text),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun cancelList() {
        val list = detailViewModel.currentShoppingList.value
        list?.let { detailViewModel.deleteList(it) }
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
            detailMemo.text = recipe.memo.toString()
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