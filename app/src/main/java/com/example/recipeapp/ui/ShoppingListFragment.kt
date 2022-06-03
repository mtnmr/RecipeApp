package com.example.recipeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.recipeapp.MyApplication
import com.example.recipeapp.R
import com.example.recipeapp.adapter.ShoppingListAdapter
import com.example.recipeapp.databinding.FragmentShoppingListBinding
import com.example.recipeapp.viewmodel.ShoppingListViewModel
//import com.example.recipeapp.viewmodel.ShoppingViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ShoppingListAdapter {
            val action =
                ShoppingListFragmentDirections.actionShoppingListFragmentToShoppingListDetailFragment(
                    it.listId
                )
            findNavController().navigate(action)
        }

        binding.shoppingListRecyclerView.adapter = adapter
        binding.shoppingListRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        viewModel.allShoppingList.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        binding.addListButton.setOnClickListener {
            viewModel.addNewShoppingList()
            val action =
                ShoppingListFragmentDirections.actionShoppingListFragmentToShoppingListDetailFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}