package com.example.demoproject

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
@HiltViewModel
class ListingViewModel  @Inject constructor(private  val preferenceDataStore: PreferenceDataStore):ViewModel() {
    lateinit var mythrowable:Throwable
    val requestHeaders = HashMap<String, String>()
    var recyclerDataArrayList=MutableLiveData<ListingData>()
    fun getlist() {
            viewModelScope.launch(Dispatchers.IO) {

                    preferenceDataStore.getFromDataStore().collect {
                        requestHeaders["access-token"]=it.access_token
                        requestHeaders["client"]=it.client
                        requestHeaders["uid"]=it.uid
                        val retrofit = Retrofit.Builder()
                            .baseUrl("https://staging.clientdex.com")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                        val retrofitAPI: RetrofitInterface =
                            retrofit.create(RetrofitInterface::class.java)
                        val call: Call<ListingData> =
                            retrofitAPI.allContacts(requestHeaders)
                        call.enqueue(object : Callback<ListingData> {
                            override fun onResponse(
                                call: Call<ListingData>,
                                response: Response<ListingData>
                            ) {

                                if (response.isSuccessful) {
                                    recyclerDataArrayList.postValue(response.body()!!)
                                    println("INListing: " + response.body().toString())
                                    Toast.makeText(
                                        MyApplication.getAppContext(),
                                        "Successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        MyApplication.getAppContext(),
                                        response.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            @SuppressLint("SetTextI18n")
                            override fun onFailure(
                                call: Call<ListingData>,
                                t: Throwable
                            ) {
                                t.also {
                                    mythrowable = it
                                }
                             println(t.message.toString())
                                Toast.makeText(
                                    MyApplication.getAppContext(),
                                    t.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }
            }
        }
}
