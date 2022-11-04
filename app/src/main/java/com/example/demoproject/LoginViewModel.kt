package com.example.demoproject

import android.annotation.SuppressLint

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private  val preferenceDataStore: PreferenceDataStore):ViewModel() {

    lateinit var mythrowable:Throwable
    val serverresponse:MutableLiveData<ServerResponse?> = MutableLiveData()
    fun login(email: String, pass: String) {
        var requestHeaders:RequestHeaders?=null
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
                        requestHeaders = RequestHeaders(
                            headers.get("uid").toString(),
                            headers.get("access-token").toString(),
                            headers.get("client").toString()
                        )

                        addDatatoStore(requestHeaders)

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
    private fun addDatatoStore(requestHeaders: RequestHeaders?)
    {

        viewModelScope.launch(Dispatchers.IO) {
            if (requestHeaders != null) {
                preferenceDataStore.savetoDataStore(
                    requestHeaders
                )
            }
        }
    }
}