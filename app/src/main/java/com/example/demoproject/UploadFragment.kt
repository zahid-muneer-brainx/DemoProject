package com.example.demoproject

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.demoproject.databinding.FragmentUploadBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class UploadFragment @Inject constructor() : Fragment() {
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    lateinit var binding: FragmentUploadBinding
    lateinit var name: EditText
    lateinit var number: EditText
    lateinit var image: ImageView
    lateinit var came: ImageView
    lateinit var updatebtn: Button
    lateinit var logoutbtn:Button
    var imageString: String = "data:image/png;base64,"
    private val Model: UpdateProfileViewModel by viewModels()
    var imageUri: Uri? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        came.setOnClickListener {
            if(checkAndRequestPermissions(requireActivity())){
            chooseImage(requireActivity())
        }
        }
        updatebtn.setOnClickListener {
            if (name.text.toString().isEmpty() || number.text.toString().length < 11) {
                Toast.makeText(requireContext(), "Invalid name or Number", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                val updateInfo = UpdateInfo(
                    number.text.toString(), imageString, name.text.toString(), "sdfdfgdfsfs",
                    "android", false, 1
                )
                Model.updateProfile(updateInfo)

            }
        }
        ResponseObserver(Model)
        logoutbtn.setOnClickListener {
            val settings: SharedPreferences =
                this.requireActivity().getSharedPreferences("login", 0) // 0 - for private mode
            val editor = settings.edit()
            editor.putBoolean("hasLoggedIn", false)
            editor.apply()
            activity?.let {
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
                Toast.makeText(requireContext(),"Logout",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadBinding.inflate(layoutInflater)
        name = binding.name
        number = binding.number
        image = binding.imagedis
        came = binding.camera
        updatebtn = binding.upbutton
        logoutbtn=binding.logout
        return binding.root
    }
    private fun ResponseObserver(model: UpdateProfileViewModel) {
        model.serverresponse.observe(viewLifecycleOwner) { serverResponse ->
            if (serverResponse == null) {
                Toast.makeText(requireContext(), "Profile Not Updated", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(
                    requireContext(),
                    "Profile Updated Successfully" + serverResponse,
                    Toast.LENGTH_SHORT
                )
                    .show()
                name.setText(null)
                number.setText(null)
                image.setImageURI(null)
            }
        }
    }

    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val WExtstorePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                .add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(), listPermissionsNeeded
                    .toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    // function to let's the user to choose image from camera or gallery
    private fun chooseImage(context: Context) {
        val optionsMenu = arrayOf<CharSequence>(
            "Take Photo",
            "Choose from Gallery",
            "Exit"
        ) // create a menuOption Array
        // create a dialog for showing the optionsMenu
        val builder = AlertDialog.Builder(context)
        // set the items in builder
        builder.setItems(
            optionsMenu
        ) { dialogInterface, i ->
            if (optionsMenu[i] == "Take Photo") {
                // Open the camera and get the photo
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0)
            } else if (optionsMenu[i] == "Choose from Gallery") {
                // choose from  external storage
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1)
            } else if (optionsMenu[i] == "Exit") {
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && data != null) {
                     imageUri = data.data
                    image.setImageURI(imageUri)
                     uriToBase64()
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    imageUri = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (imageUri != null) {
                        val cursor: Cursor? =requireActivity().contentResolver.query(
                            imageUri!!,
                            filePathColumn,
                            null,
                            null,
                            null
                        )

                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath: String = cursor.getString(columnIndex)
                            image.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                            cursor.close()
                          uriToBase64()
                        }
                    }
                }

            }
        }
    }
private fun uriToBase64()
{
    try {
        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bytes: ByteArray = stream.toByteArray()
        imageString += Base64.encodeToString(bytes, Base64.DEFAULT)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}
}