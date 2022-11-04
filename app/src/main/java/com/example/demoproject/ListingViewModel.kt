package com.example.demoproject

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    var recyclerDataArrayList=MutableLiveData<ArrayList<ListingData>>()

//    lateinit var requestHeaders:RequestHeaders
    val serverresponse: MutableLiveData<ServerResponse?> = MutableLiveData()

    fun getlist(requestHeaders: RequestHeaders) {
            viewModelScope.launch(Dispatchers.IO) {
                println("111111111111111111")
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://staging.clientdex.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val retrofitAPI: RetrofitInterface = retrofit.create(RetrofitInterface::class.java)
                val call: Call<ArrayList<ListingData>> = retrofitAPI.allContacts(requestHeaders)
                call.enqueue(object : Callback<ArrayList<ListingData>> {
                    override fun onResponse(
                        call: Call<ArrayList<ListingData>>,
                        response: Response<ArrayList<ListingData>>
                    ) {

                        if (response.isSuccessful) {
                            recyclerDataArrayList.postValue( response.body()!!)
                            println("INListing: "+response.body().toString())
                            Toast.makeText(MyApplication.getAppContext(),"Successful",Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            Toast.makeText(MyApplication.getAppContext(),response.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onFailure(call: Call<ArrayList<ListingData>>, t: Throwable) {
                        t.also {
                            mythrowable = it
                        }

                         Toast.makeText(MyApplication.getAppContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
}
