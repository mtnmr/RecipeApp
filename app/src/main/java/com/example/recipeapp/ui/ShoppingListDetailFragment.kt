package com.example.recipeapp.ui

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.recipeapp.MyApplication
import com.example.recipeapp.adapter.ListDetailAdapter
import com.example.recipeapp.data.ShoppingList
import com.example.recipeapp.databinding.FragmentShoppingListDetailBinding
import com.example.recipeapp.viewmodel.ListDetailViewModel
import com.example.recipeapp.viewmodel.ListDetailViewModelFactory

class ShoppingListDetailFragment : Fragment() {

    private var _binding : FragmentShoppingListDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListDetailViewModel by activityViewModels {
        ListDetailViewModelFactory((activity?.application as MyApplication).repository)
    }

    private lateinit var shoppingList : ShoppingList

    private val args:ShoppingListDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingListDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListDetailAdapter{
            viewModel.changeChecked(it)
        }

        binding.shoppingListDetailRecyclerView.adapter = adapter
        binding.shoppingListDetailRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        val id = args.itemId
        if (id >= 0) {
            viewModel.getShoppingList(id).observe(viewLifecycleOwner) {
                shoppingList = it
                binding.shoppingListTitleEdit.setText(it.listTitle)
                viewModel.changeCurrentId(it.listId)
            }
        }else{
            viewModel.currentShoppingList.observe(viewLifecycleOwner){
                shoppingList = it
                viewModel.changeCurrentId(shoppingList.listId)
            }
        }

        viewModel.currentListDetails.observe(viewLifecycleOwner){items ->
            items.let {
                adapter.submitList(it)
            }

        }

        binding.shoppingListTitleEdit.setOnKeyListener{view, keycode, _ ->
            handleKeyEvent(view, keycode)
        }


        binding.addDetailButton.setOnClickListener {
            val word = binding.addDetailEdit.text.toString()
            viewModel.addNewDetail(shoppingList.listId, word)
            binding.addDetailEdit.text?.clear()
        }


        binding.listDeleteButton.setOnClickListener {
            viewModel.deleteList(shoppingList)
            val action = ShoppingListDetailFragmentDirections.actionShoppingListDetailFragmentToShoppingListFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleKeyEvent(view:View, keyCode:Int): Boolean{
        if (keyCode == KeyEvent.KEYCODE_ENTER){
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            val title = binding.shoppingListTitleEdit.text
            viewModel.updateShoppingList(args.itemId, title.toString())
            return true
        }
        return false
    }
}