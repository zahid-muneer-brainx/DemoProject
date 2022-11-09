package com.example.demoproject

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(private val preferenceDataStore: PreferenceDataStore) :
    ViewModel() {
    val serverresponse: MutableLiveData<ServerResponseModel?> = MutableLiveData()
    val requestHeaders = HashMap<String, String>()
    fun updateProfile(updateProfileInfoModel: UpdateProfileInfoModel) {
        viewModelScope.launch {
            preferenceDataStore.getFromDataStore().collect {
                requestHeaders["access-token"] = it.access_token
                requestHeaders["client"] = it.client
                requestHeaders["uid"] = it.uid
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://staging.clientdex.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val retrofitAPI: RetrofitInterface =
                    retrofit.create(RetrofitInterface::class.java)
                val call: Call<ServerResponseModel?> = retrofitAPI.update(requestHeaders, updateProfileInfoModel)
                call.enqueue(object : Callback<ServerResponseModel?> {
                    override fun onResponse(
                        call: Call<ServerResponseModel?>,
                        response: Response<ServerResponseModel?>
                    ) {

                        if (response.isSuccessful) {
                            serverresponse.postValue(response.body())

                        } else {
                            println("failure")
                            serverresponse.postValue(null)

                        }
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onFailure(call: Call<ServerResponseModel?>, t: Throwable) {
                        t.also {
                            Toast.makeText(
                                MyApplication.getAppContext(),
                                t.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    }
                })
            }
        }
    }

}