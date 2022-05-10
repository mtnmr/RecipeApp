package com.example.recipeapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRecipeEditBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.selects.whileSelect
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class RecipeEditFragment : Fragment() {

    private var _binding: FragmentRecipeEditBinding? = null
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

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val spinner = parent as Spinner
                val str = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        binding.recipeImage.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.image_select)
                .setItems(R.array.storage_or_camera){ dialog, which ->
                    when(which){
                        0 -> checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        1 -> checkPermission(Manifest.permission.CAMERA)
                    }
                }
                .show()
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

    private val selectPhotoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                return@registerForActivityResult
            } else {
                try {
                    binding.recipeImage.setImageURI(result.data?.data)
                } catch (e: Exception) {
                    Toast.makeText(
                        activity?.applicationContext,
                        "select photo Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        selectPhotoLauncher.launch(intent)
    }

    private var cameraUri: Uri? = null

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
//                            Log.d("recipe app", "$cameraUri")
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