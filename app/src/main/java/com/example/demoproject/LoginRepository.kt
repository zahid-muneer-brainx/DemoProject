package com.example.demoproject

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class LoginRepository @Inject constructor(var  preferenceDataStore: PreferenceDataStore) {

    var requestHeadersModel: RequestHeadersModel? = null
     fun addDatatoStore(requestHeadersModel: RequestHeadersModel?) {
     runBlocking(Dispatchers.IO) {
         if (requestHeadersModel != null) {
             preferenceDataStore.savetoDataStore(requestHeadersModel)
         }
     }
    }
     fun login(serverresponse: MutableLiveData<ServerResponseModel?>,email: String, pass: String)
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://staging.clientdex.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitAPI: RetrofitInterface = retrofit.create(RetrofitInterface::class.java)
        val dataModal = UserInfoModel(email, pass)

        val call: Call<ServerResponseModel?> = retrofitAPI.login(dataModal)

        call.enqueue(object : Callback<ServerResponseModel?> {
            override fun onResponse(
                call: Call<ServerResponseModel?>,
                response: Response<ServerResponseModel?>
            ) {

                if (response.isSuccessful) {
                    val headers = response.headers()
                    requestHeadersModel = RequestHeadersModel(
                        headers.get("uid").toString(),
                        headers.get("access-token").toString(),
                        headers.get("client").toString()
                    )
                    serverresponse.postValue(response.body())
                    addDatatoStore(requestHeadersModel)
                } else {
                    serverresponse.postValue(null)
                }
            }

            @SuppressLint("SetTextI18n")
            override fun onFailure(call: Call<ServerResponseModel?>, t: Throwable) {


                //     Toast.makeText(AndroidViewModel,t.message.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }
}