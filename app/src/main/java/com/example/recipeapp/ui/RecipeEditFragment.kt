package com.example.recipeapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.recipeapp.R
import com.example.recipeapp.data.Recipe
import com.example.recipeapp.databinding.FragmentRecipeEditBinding
import com.example.recipeapp.viewmodel.RecipeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RecipeEditFragment : Fragment() {

    private var _binding: FragmentRecipeEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by activityViewModels()

    private val args: RecipeDetailFragmentArgs by navArgs()

    private lateinit var recipe: Recipe

    private var category: Int = 0
    private var cameraUri: Uri? = null

    private var favorite = false

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

        val id = args.itemId
        if (id > 0) {
            viewModel.getRecipe(id).observe(viewLifecycleOwner) { item ->
                recipe = item
                favorite = recipe.isFavorite
                favoriteChange(favorite)
                bind(recipe)
            }
        } else {
            binding.recipeSaveButton.setOnClickListener {
                addNewRecipe()
            }
        }

        binding.favoriteButton.setOnClickListener {
            favorite = !favorite
            favoriteChange(favorite)
        }

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.recipe_category_list,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinner = binding.categorySpinner
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val spinner = parent as Spinner
                category = pos
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.recipeImage.setOnClickListener { recipeImageDialog() }

        binding.recipeDateEdit.setOnClickListener { showDatePicker(binding.recipeDateEdit) }
    }


    private fun favoriteChange(favorite: Boolean) {
        if (favorite) {
            binding.favoriteButton.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
        } else {
            binding.favoriteButton.colorFilter = null
        }
    }

    private fun bind(recipe: Recipe) {
        binding.apply {
            recipeTitleEdit.setText(recipe.title)
            categorySpinner.setSelection(recipe.category)
            cameraUri = recipe.image?.toUri()
            if (recipe.image.toString() != "null") {
                recipeImage.setImageURI(cameraUri)
            }
            recipeIngredientsEdit.setText(recipe.ingredients.toString())
            recipeMemoEdit.setText(recipe.memo.toString())
            recipeLinkEdit.setText(recipe.link.toString())
            recipeDateEdit.setText(recipe.date.toString())
            recipeDeleteButton.visibility = View.VISIBLE

            recipeDeleteButton.setOnClickListener {
                viewModel.delete(recipe)
                val action = RecipeEditFragmentDirections.actionRecipeEditFragmentToHomeFragment()
                findNavController().navigate(action)
                Toast.makeText(requireContext(), "レシピを削除しました", Toast.LENGTH_SHORT).show()
            }

            recipeSaveButton.setOnClickListener {
                updateRecipe()
            }
        }
    }

    private fun addNewRecipe() {
        if (binding.recipeTitleEdit.text.toString().isNotEmpty()) {
            viewModel.addNewRecipe(
                title = binding.recipeTitleEdit.text.toString(),
                category = category,
                image = cameraUri.toString(),
                ingredients = binding.recipeIngredientsEdit.text.toString(),
                link = binding.recipeLinkEdit.text.toString(),
                date = binding.recipeDateEdit.text.toString(),
                memo = binding.recipeMemoEdit.text.toString(),
                isFavorite = favorite
            )

            val action = RecipeEditFragmentDirections.actionRecipeEditFragmentToHomeFragment()
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "タイトルが未入力です", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateRecipe() {
        viewModel.updateRecipe(
            id = args.itemId,
            title = binding.recipeTitleEdit.text.toString(),
            category = category,
            image = cameraUri.toString(),
            ingredients = binding.recipeIngredientsEdit.text.toString(),
            link = binding.recipeLinkEdit.text.toString(),
            date = binding.recipeDateEdit.text.toString(),
            memo = binding.recipeMemoEdit.text.toString(),
            isFavorite = favorite
        )
        val action = RecipeEditFragmentDirections.actionRecipeEditFragmentToHomeFragment()
        findNavController().navigate(action)
    }


    private fun recipeImageDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.image_select)
            .setItems(R.array.storage_or_camera) { _, which ->
                when (which) {
                    0 -> checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    1 -> checkPermission(Manifest.permission.CAMERA)
                    2 -> deletePhoto()
                }
            }
            .show()
    }

    private fun deletePhoto(){
        cameraUri = null
        binding.recipeImage.setImageResource(R.drawable.ic_baseline_image_24)
    }

    //パーミッション許可
    private fun checkPermission(permission: String) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            when (permission) {
                Manifest.permission.READ_EXTERNAL_STORAGE -> selectPhoto()
                Manifest.permission.CAMERA -> takePicture()
            }
        } else {
            requestPermission(permission)
        }
    }

    private fun requestPermission(permission: String) {
        when (permission) {
            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        permission
                    )
                ) {
                    storagePermissionLauncher.launch(permission)
                } else {
                    //説明を表示する処理？
                    storagePermissionLauncher.launch(permission)
                }
            }

            Manifest.permission.CAMERA -> {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        permission
                    )
                ) {
                    cameraPermissionLauncher.launch(permission)
                } else {
                    //説明を表示する処理？
                    cameraPermissionLauncher.launch(permission)
                }
            }
        }
    }

    private val storagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
//                Toast.makeText(
//                    activity?.applicationContext,
//                    "デバイス内の写真やメディアへのアクセスが許可されました。",
//                    Toast.LENGTH_SHORT
//                ).show()
                Log.d("recipeApp", "permission OK")
                selectPhoto()
            } else {
                Toast.makeText(
                    activity?.applicationContext,
                    "デバイス内の写真やメディアへのアクセスが許可されませんでした。",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("recipeApp", "permission")
            }
        }

    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
//                Toast.makeText(
//                    activity?.applicationContext,
//                    "デバイス内の写真やメディアへのアクセスが許可されました。",
//                    Toast.LENGTH_SHORT
//                ).show()
                Log.d("recipeApp", "camera permission OK")
                takePicture()
            } else {
                Toast.makeText(
                    activity?.applicationContext,
                    "カメラへのアクセスが許可されませんでした。",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("recipeApp", "camera permission NG")
            }
        }


    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        selectPhotoLauncher.launch(intent)
    }

    private val selectPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                return@registerForActivityResult
            } else {
                try {
                    cameraUri = result.data?.data
                    binding.recipeImage.setImageURI(cameraUri)

                    //ファイルへのアクセス権を永続的に保持
                    val contentResolver = activity?.applicationContext?.contentResolver
                    val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    contentResolver?.takePersistableUriPermission(cameraUri!!, takeFlags)

                } catch (e: Exception) {
                    Toast.makeText(
                        activity?.applicationContext,
                        "select photo Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private fun takePicture() {
//        val dir:File? = activity?.applicationContext?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        //内部ストレージPicturesに保存される
//        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        //内部ストレージDCIMに保存される　→　画像から見れる
        val dir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera")
        val fileDate: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())
        val file: File = File.createTempFile("JPEG_${fileDate}_", ".jpg", dir)
        cameraUri = FileProvider.getUriForFile(
            activity?.applicationContext!!,
            "com.example.recipeapp.fileprovider",
            file
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        }
        takePictureLauncher.launch(intent)
        Log.d("recipe app", "take picture intent")

    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                return@registerForActivityResult
            } else {
                try {
                    val data = result.data
                    if (data != null) {
                        if (cameraUri != null) {
                            binding.recipeImage.setImageURI(cameraUri)
                            Log.d("recipe app", "$cameraUri")
                        } else {
                            Log.d("recipe app", "cameraUri null")
                        }
                    }
//                    val data = result.data?.extras?.get("data") as Bitmap
//                    if (data != null){
//                        binding.recipeImage.setImageBitmap(data)
//                        Log.d("recipe app", "picture set")
//                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        activity?.applicationContext,
                        "set photo Error",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("recipe app", e.toString())
                }
            }
        }



    //日付入力
    private fun showDatePicker(editText: EditText) {
        val c = Calendar.getInstance()
        val nowYear = c.get(Calendar.YEAR)
        val nowMonth = c.get(Calendar.MONTH)
        val nowDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                editText.setText(String.format("%d/%02d/%02d", year, month + 1, day))
            },
            nowYear,
            nowMonth,
            nowDay
        )

        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}