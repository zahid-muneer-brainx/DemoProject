package com.example.demoproject

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.demoproject.databinding.FragmentUploadBinding
import dagger.hilt.android.AndroidEntryPoint
import okio.IOException
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class UploadFragment @Inject constructor(): Fragment() {
    private  val PICK_IMAGE = 100
    private  val STORAGE_PERMISSION_CODE = 101
    lateinit var binding: FragmentUploadBinding
     lateinit var name:EditText
     lateinit var number:EditText
     lateinit var image:ImageView
     lateinit var came:ImageView
     lateinit var updatebtn:Button
     var imageString:String="data:image/png;base64,"
    private val Model: UpdateProfileViewModel by viewModels()
    var imageUri: Uri? = null
    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        came.setOnClickListener {
            if(checkPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    STORAGE_PERMISSION_CODE))
            {
                openGallery()
            }
        }
        updatebtn.setOnClickListener {
              if(name.text.toString().isEmpty() || number.text.toString().length<11) {
                  Toast.makeText(requireContext(), "Invalid name or Number", Toast.LENGTH_SHORT)
                      .show()
                  return@setOnClickListener
              }
            else
              {
                  val updateInfo=UpdateInfo(number.text.toString(),imageString,name.text.toString(),"sdfdfgdfsfs",
                  "android",false,1)
                  Model.updateProfile(updateInfo)

              }
        }
        ResponseObserver(Model)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentUploadBinding.inflate(layoutInflater)
        name=binding.name
        number=binding.number
        image=binding.imagedis
        came=binding.camera
        updatebtn=binding.upbutton
        return binding.root
    }
    private fun checkPermission(permission: String, requestCode: Int):Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_DENIED) {


            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
            return false
        } else {
            Toast.makeText(requireContext(), "Permission already granted", Toast.LENGTH_SHORT).show()
            return true
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)

    }
    @Deprecated("Deprecated in Java")
    override  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == PICK_IMAGE) {

            imageUri = data?.data
            image.setImageURI(imageUri)
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
                // initialize byte stream
                val stream = ByteArrayOutputStream()
                // compress Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                // Initialize byte array
                val bytes = stream.toByteArray()
                // get base64 encoded string
                imageString += Base64.encodeToString(bytes, Base64.DEFAULT)
                // set encoded text on textview

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun ResponseObserver(model:UpdateProfileViewModel)
    {
        model.serverresponse.observe(viewLifecycleOwner){ serverResponse ->
            if(serverResponse==null) {
                Toast.makeText(requireContext(), "Profile Not Updated", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(requireContext(), "Profile Updated Successfully"+serverResponse, Toast.LENGTH_SHORT)
                    .show()
                name.setText(null)
                number.setText(null)
                image.setImageURI(null)
            }
        }
    }
}