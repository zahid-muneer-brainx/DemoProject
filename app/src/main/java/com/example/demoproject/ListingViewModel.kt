package com.example.demoproject

import DataStoreViewModel
import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ListingViewModel:ViewModel() {

    lateinit var requestHeaders:RequestHeaders
    lateinit var mythrowable:Throwable
    private var recyclerDataArrayList: ArrayList<ListingData>? = null
    @Inject
    lateinit var dataStoreViewModel : DataStoreViewModel

    val serverresponse: MutableLiveData<ServerResponse?> = MutableLiveData()
    fun getlist():ArrayList<ListingData>? {
        requestHeaders = RequestHeaders(
            dataStoreViewModel.getUserid(),
            dataStoreViewModel.getUserAccessToken(),
            dataStoreViewModel.getUserclient()
        )
        viewModelScope.launch() {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://staging.clientdex.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val retrofitAPI: RetrofitInterface = retrofit.create(RetrofitInterface::class.java)

            val call: Call<ArrayList<ListingData>> = retrofitAPI.allContacts(requestHeaders)

            call.enqueue(object : Callback<ArrayList<ListingData>?> {
                override fun onResponse(
                    call: Call<ArrayList<ListingData>?>,
                    response: Response<ArrayList<ListingData>?>
                ) {

                    if (response.isSuccessful) {
                        recyclerDataArrayList = response.body()

                    } else {
                        serverresponse.postValue(null)
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<ArrayList<ListingData>?>, t: Throwable) {
                    t.also {
                        mythrowable = it
                    }

                    //     Toast.makeText(AndroidViewModel,t.message.toString(),Toast.LENGTH_SHORT).show()
                }
            })
        }
        return recyclerDataArrayList
    }
}