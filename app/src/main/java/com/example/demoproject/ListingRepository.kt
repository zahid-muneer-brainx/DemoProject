package com.example.demoproject

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ListingRepository @Inject constructor(private val preferenceDataStore: PreferenceDataStore) {
    private val requestHeaders = HashMap<String, String>()
    suspend fun getlist(mydata: MutableLiveData<ListingDataModel>) {

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
            val call: Call<ListingDataModel> =
                retrofitAPI.allContacts(requestHeaders)
            call.enqueue(object : Callback<ListingDataModel> {
                override fun onResponse(
                    call: Call<ListingDataModel>,
                    response: Response<ListingDataModel>
                ) {

                    if (response.isSuccessful) {
                        mydata.postValue(response.body())
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
                    call: Call<ListingDataModel>,
                    t: Throwable
                ) {
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

    suspend fun searchByName(name: String?, mydata: MutableLiveData<ListingDataModel>) {
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
            val call: Call<ListingDataModel> =
                retrofitAPI.search(requestHeaders, name)
            call.enqueue(object : Callback<ListingDataModel> {
                override fun onResponse(
                    call: Call<ListingDataModel>,
                    response: Response<ListingDataModel>
                ) {

                    if (response.isSuccessful) {
                        mydata.postValue(response.body()!!)
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
                    call: Call<ListingDataModel>,
                    t: Throwable
                ) {
                    t.also {
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