package com.example.recipeapp.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.recipeapp.MyApplication
import com.example.recipeapp.R
import com.example.recipeapp.adapter.ListDetailAdapter
import com.example.recipeapp.adapter.MyItemDetailsLookup
import com.example.recipeapp.data.ShoppingList
import com.example.recipeapp.databinding.FragmentShoppingListDetailBinding
import com.example.recipeapp.viewmodel.ListDetailViewModel
//import com.example.recipeapp.viewmodel.ListDetailViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingListDetailFragment : Fragment() {

    private var _binding: FragmentShoppingListDetailBinding? = null
    private val binding get() = _binding!!


    private val viewModel: ListDetailViewModel by activityViewModels()

    private lateinit var shoppingList: ShoppingList

    private val args: ShoppingListDetailFragmentArgs by navArgs()


    private lateinit var tracker: SelectionTracker<Long>
    private var actionMode: ActionMode? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingListDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListDetailAdapter(
            onItemClicked = { viewModel.changeChecked(it) }
        )

        val mRecyclerView = binding.shoppingListDetailRecyclerView
        mRecyclerView.adapter = adapter
        mRecyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        val id = args.itemId
        if (id >= 0) {
            viewModel.getShoppingList(id).observe(viewLifecycleOwner) {
                shoppingList = it
                binding.shoppingListTitleEdit.setText(it.listTitle)
                viewModel.changeCurrentId(it.listId)
            }
        } else {
            viewModel.currentShoppingList.observe(viewLifecycleOwner) {
                shoppingList = it
                viewModel.changeCurrentId(shoppingList.listId)
            }
        }

        viewModel.currentListDetails.observe(viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }

        }

        binding.shoppingListTitleEdit.setOnKeyListener { view, keycode, _ ->
            handleKeyEvent(view, keycode)
        }


        binding.addDetailButton.setOnClickListener {
            val word = binding.addDetailEdit.text.toString()
            viewModel.addNewDetail(shoppingList.listId, word)
            binding.addDetailEdit.text?.clear()
        }


        binding.listDeleteButton.setOnClickListener {
            viewModel.deleteList(shoppingList)
            viewModel.deleteListDetails(shoppingList.listId)
            val action =
                ShoppingListDetailFragmentDirections.actionShoppingListDetailFragmentToShoppingListFragment()
            findNavController().navigate(action)
        }

        tracker = SelectionTracker.Builder(
            "ListTracker",
            mRecyclerView,
            StableIdKeyProvider(mRecyclerView),
            MyItemDetailsLookup(mRecyclerView),
            StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()

        adapter.tracker = tracker

        tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                if (tracker.selection.size() >= 1 && actionMode == null) {
                    actionMode = requireActivity().startActionMode(object : ActionMode.Callback {
                        override fun onActionItemClicked(
                            mode: ActionMode?,
                            item: MenuItem?
                        ): Boolean {
                            return when (item?.itemId) {
                                R.id.delete -> {
                                    deleteDetails(tracker.selection)
                                    mode?.finish()
                                    true
                                }else -> {
                                    true
                                }
                            }
                        }

                        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                            mode?.menuInflater?.inflate(R.menu.tracker_menu, menu)
                            return true
                        }

                        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                            return false
                        }

                        override fun onDestroyActionMode(mode: ActionMode?) {
                            actionMode = null
                            if (tracker.hasSelection()) {
                                tracker.clearSelection()
                                adapter.notifyDataSetChanged()
                            }
                        }
                    })
                } else if (!tracker.hasSelection()) {
                    actionMode?.finish()
                }
            }

            override fun onSelectionRestored() {
                super.onSelectionRestored()

                // 復元後アクションモードの起動を判断するため
                this.onSelectionChanged()
            }
        })
    }

    private fun deleteDetails(selection : Selection<Long>){
        if (selection.isEmpty){
            return
        }else{
            selection.forEach {
                val id = it.toInt()
                viewModel.deleteDetail(id)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            val title = binding.shoppingListTitleEdit.text
            viewModel.updateShoppingList(args.itemId, title.toString())
            return true
        }
        return false
    }
}