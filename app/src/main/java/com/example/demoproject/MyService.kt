package com.example.demoproject


import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject

class MyService : Service() {
    @Inject
    lateinit var preferenceDataStore: PreferenceDataStore
    var mbinder: MyBinder = MyBinder()
    override fun onBind(p0: Intent?): IBinder? {
        return mbinder
    }

    inner class MyBinder : Binder() {
        fun getService(): MyService {
            return this@MyService
        }
    }
     fun uriToBase64(imageUri:Uri?):String {
        try {
            val bitmap =
                MediaStore.Images.Media.getBitmap(MyApplication.appInstance.contentResolver, imageUri)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bytes: ByteArray = stream.toByteArray()
            return  Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
}
