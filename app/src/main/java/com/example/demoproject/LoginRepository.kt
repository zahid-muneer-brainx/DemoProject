package com.example.demoproject

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(
    var preferenceDataStore: PreferenceDataStore,
    retrofitRepository: RetrofitRepository
) {
    val failedResponse=MutableLiveData<String>()
    var requestHeadersModel: RequestHeadersModel? = null
    private val retrofitAPI = retrofitRepository.getretrofitApi()


    fun login(serverResponse: MutableLiveData<ServerResponseModel?>, email: String, pass: String) {
        val dataModal = UserInfoModel(email, pass)
        val call: Call<ServerResponseModel?> = retrofitAPI.login(dataModal)

        call.enqueue(object : Callback<ServerResponseModel?> {
            override fun onResponse(
                call: Call<ServerResponseModel?>,
                response: Response<ServerResponseModel?>
            ) {

                if (response.isSuccessful) {
                    val headers=response.headers()
                    requestHeadersModel= RequestHeadersModel(
                    "application/json; charset=UTF-8",
                    headers.get("uid").toString(),
                    headers.get("access-token").toString(),
                        headers.get("client").toString()
                    )
                    println("After login: $requestHeadersModel")
                    serverResponse.postValue(response.body())
                    addDataToStore(requestHeadersModel)
                } else {
                    serverResponse.postValue(null)
                }
            }


            override fun onFailure(call: Call<ServerResponseModel?>, t: Throwable) {
                failedResponse.postValue(t.message.toString())
            }
        })
    }
    fun addDataToStore(requestHeadersModel: RequestHeadersModel?) {
        runBlocking(Dispatchers.IO) {
            if (requestHeadersModel != null) {
                preferenceDataStore.savetoDataStore(requestHeadersModel)
            }
        }
    }

}