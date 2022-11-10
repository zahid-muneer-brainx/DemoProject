package com.example.demoproject

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.demoproject.databinding.FragmentUploadBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class UploadFragment @Inject constructor() : Fragment() {
    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore
    var myService: MyService? = null
    var isBound = false
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    lateinit var binding: FragmentUploadBinding
    private var imageString: String = "data:image/png;base64,"
    private val myConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            val binder = service as MyService.MyBinder
            myService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            isBound = false
        }
    }
    private val model: UpdateProfileViewModel by viewModels()
    var imageBitmap: Bitmap? = null
    var imageUri: Uri? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.camera.setOnClickListener {
            if (checkAndRequestPermissions(requireActivity())) {
                chooseImage(requireActivity())
            }
        }
        binding.upbutton.setOnClickListener {
            if (binding.name.text.toString()
                    .isEmpty() || binding.number.text.toString().length < 11
            ) {
                Toast.makeText(requireContext(), "Invalid name or Number", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                val updateProfileInfoModel = UpdateProfileInfoModel(
                    binding.number.text.toString(),
                    imageString,
                    binding.name.text.toString(),
                    "sdfdfgdfsfs",
                    "android",
                    false,
                    1
                )
                if (isBound) {
                    model.updateProfile(updateProfileInfoModel)
                    ResponseObserver()
                }
            }
        }

        binding.logout.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                preferenceDataStore.saveLoginStatus(false)
            }
            activity?.let {
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
                Toast.makeText(requireContext(), "Logout", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadBinding.inflate(layoutInflater)
        Intent(requireContext(), MyService::class.java).also { intent ->
            activity?.bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
        }
        return binding.root
    }

    private fun ResponseObserver() {
        model.serverresponse.observe(viewLifecycleOwner) { serverResponse ->
            if (serverResponse == null) {
                Toast.makeText(requireContext(), "Profile Not Updated", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(
                    requireContext(),
                    "Profile Updated Successfully $serverResponse",
                    Toast.LENGTH_SHORT
                )
                    .show()
                binding.name.setText(null)
                binding.number.setText(null)
            }
        }
    }

    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val rExtstorePermission = ContextCompat.checkSelfPermission(
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
        if (rExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                .add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
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
                    println("Image from Camera 0")
                    imageBitmap = data.extras!!["data"] as Bitmap?
                    binding.imagedis.setImageBitmap(imageBitmap)
                    if (isBound) {
                        imageString += myService?.uriToBase64(
                            imageBitmap,
                            requireActivity().contentResolver
                        )
                        println("Base 64: $imageString")
                    }
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    imageUri = data.data
                    binding.imagedis.setImageURI(imageUri)
                    try {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                imageUri
                            )
                        // initialize byte stream
                        val stream = ByteArrayOutputStream()
                        // compress Bitmap
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                        // Initialize byte array
                        val bytes = stream.toByteArray()
                        // get base64 encoded string
                        imageString += Base64.encodeToString(bytes, Base64.DEFAULT)
                        // set encoded text on textview
                        if (isBound) {
                            imageString += myService?.uriToBase64(
                                bitmap,
                                requireActivity().contentResolver
                            )
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to LocalService
        Intent(requireContext(), MyService::class.java).also { intent ->
            activity?.bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
        }
        isBound = true
    }
}
