package com.example.demoproject

import DataStoreViewModel
import android.annotation.SuppressLint
import androidx.activity.viewModels
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import kotlin.math.log
@HiltViewModel
class LoginViewModel @Inject constructor():ViewModel() {

    lateinit var mythrowable:Throwable
    @Inject lateinit var dataStoreViewModel : DataStoreViewModel

    val serverresponse:MutableLiveData<ServerResponse?> = MutableLiveData()
    fun login(email: String, pass: String) {
        viewModelScope.launch() {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://staging.clientdex.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val retrofitAPI: RetrofitInterface = retrofit.create(RetrofitInterface::class.java)
             val dataModal=UserInfo(email,pass)

            val call: Call<ServerResponse?> = retrofitAPI.login(dataModal)

            call.enqueue(object : Callback<ServerResponse?> {
                override fun onResponse(
                    call: Call<ServerResponse?>,
                    response: Response<ServerResponse?>
                ) {

                    if (response.isSuccessful) {
                        val headers=response.headers()
                        dataStoreViewModel.storeAccessToken(headers.get("access-token").toString())
                        dataStoreViewModel.storeUserid(headers.get("uid").toString())
                        dataStoreViewModel.storeUserclient(headers.get("client").toString())
                        serverresponse.postValue(response.body())
                    } else {
                        serverresponse.postValue(null)
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {
                    t.also {
                        mythrowable = it
                    }

                    //     Toast.makeText(AndroidViewModel,t.message.toString(),Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}