package com.example.demoproject

import android.annotation.SuppressLint

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject



@HiltViewModel
class LoginViewModel @Inject constructor (private var loginRepository: LoginRepository) :
    ViewModel() {

    val serverresponse: MutableLiveData<ServerResponseModel?> = MutableLiveData()
    fun login(email: String, pass: String) {

        viewModelScope.launch() {
            loginRepository.login(serverresponse, email, pass)
            println("Headers: "+loginRepository.requestHeadersModel.toString())
        }


    }

}