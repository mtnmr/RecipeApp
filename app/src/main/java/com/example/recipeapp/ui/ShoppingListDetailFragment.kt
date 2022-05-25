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
import com.example.recipeapp.MyApplication
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentShoppingListDetailBinding
import com.example.recipeapp.viewmodel.ShoppingViewModel
import com.example.recipeapp.viewmodel.ShoppingViewModelFactory

class ShoppingListDetailFragment : Fragment() {

    private var _binding : FragmentShoppingListDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingViewModel by activityViewModels {
        ShoppingViewModelFactory((activity?.application as MyApplication).repository)
    }

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

        val id = args.itemId
        viewModel.getShoppingList(id).observe(this.viewLifecycleOwner){
            binding.shoppingListTitleEdit.setText(it.listTitle.toString())
        }

        binding.shoppingListTitleEdit.setOnKeyListener{view, keycode, _ ->
            handleKeyEvent(view, keycode)
        }


        binding.addDetailButton.setOnClickListener {
            binding.addDetailEdit.text?.clear()
        }

        binding.listDeleteButton.setOnClickListener {

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