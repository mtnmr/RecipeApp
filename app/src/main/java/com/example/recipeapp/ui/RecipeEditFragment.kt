package com.example.recipeapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.input.InputManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRecipeEditBinding
import java.lang.Exception
import java.util.*

class RecipeEditFragment : Fragment() {

    private var _binding:FragmentRecipeEditBinding ?= null
    private val binding get() = _binding!!

//    private val RECORD_REQUEST_CODE  = 1000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.recipe_category_list,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = binding.categorySpinner
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object :  AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val spinner = parent as Spinner
                val str = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        binding.recipeImage.setOnClickListener {
            checkPermission()
        }


        binding.recipeDateEdit.setOnClickListener { showDatePicker(binding.recipeDateEdit) }


        binding.recipeDeleteButton.setOnClickListener {
            val action = RecipeEditFragmentDirections.actionRecipeEditFragmentToHomeFragment()
            findNavController().navigate(action)
        }

        binding.recipeSaveButton.setOnClickListener {
            val action = RecipeEditFragmentDirections.actionRecipeEditFragmentToHomeFragment()
            findNavController().navigate(action)
        }

    }

    //パーミッション許可
    private fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            selectPhoto()
        }else{
           requestPermission()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted:Boolean ->
            if (isGranted){
                Toast.makeText(activity?.applicationContext, "デバイス内の写真やメディアへのアクセスが許可されました。", Toast.LENGTH_SHORT).show()
                Log.d("recipeApp", "permission OK")
                selectPhoto()
            }else{
                Toast.makeText(activity?.applicationContext , "デバイス内の写真やメディアへのアクセスが許可されませんでした。", Toast.LENGTH_SHORT).show()
                Log.d("recipeApp", "permission")
            }
        }

    private fun requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }else{
            //説明を表示する処理？
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private val selectPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode != RESULT_OK){
                return@registerForActivityResult
            }else{
                try{
                    binding.recipeImage.setImageURI(result.data?.data)
                }catch (e:Exception){
                    Toast.makeText(activity?.applicationContext, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun selectPhoto(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        selectPhotoLauncher.launch(intent)
    }


    //上手く結果が取れなかった？Log出力できず
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        when (requestCode){
//            RECORD_REQUEST_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(activity?.applicationContext, "デバイス内の写真やメディアへのアクセスが許可されました。", Toast.LENGTH_SHORT).show()
//                    Log.d("recipeApp", "permission OK")
//                }else{
//                    Toast.makeText(activity?.applicationContext , "デバイス内の写真やメディアへのアクセスが許可されませんでした。", Toast.LENGTH_SHORT).show()
//                    Log.d("recipeApp", "permission")
//                }
//                return
//            }
//        }
//    }


    //日付入力
    private fun showDatePicker(editText: EditText){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                editText.setText(String.format("%d/%02d/%02d", year, month+1, day))
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}