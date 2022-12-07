package com.example.demoproject

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.onEach
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ListingRepository @Inject constructor(
    private val preferenceDataStore: PreferenceDataStore,
    retrofitRepository: RetrofitRepository
) {
    private val requestHeaders = HashMap<String, String>()
    private val retrofitAPI = retrofitRepository.getretrofitApi()
    val failedResponse=MutableLiveData<String>()
    fun getList(myData: MutableLiveData<ListingDataModel>, page: Int) {
        println("Headers: $requestHeaders")
        val call: Call<ListingDataModel> = retrofitAPI.allContacts(requestHeaders, page)
        call.enqueue(object : Callback<ListingDataModel> {
            override fun onResponse(
                call: Call<ListingDataModel>,
                response: Response<ListingDataModel>
            ) {

                if (response.isSuccessful) {
                    if (page > 1) {
                        response.body()
                            ?.let { myData.value?.cardContactModels?.addAll(it.cardContactModels) }
                    } else {
                        myData.postValue(response.body())
                    }
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
               failedResponse.postValue(t.message.toString())
            }
        })
    }

    fun searchByName(name: String?, mydata: MutableLiveData<ListingDataModel>, page: Int) {
        val call: Call<ListingDataModel> =
            retrofitAPI.search(requestHeaders, name, page)
        call.enqueue(object : Callback<ListingDataModel> {
            override fun onResponse(
                call: Call<ListingDataModel>,
                response: Response<ListingDataModel>
            ) {

                if (response.isSuccessful) {
                    mydata.postValue(response.body()!!)
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
                failedResponse.postValue(t.message.toString())
            }
        })

    }

    suspend fun collectFromDatastore() {
        requestHeaders["Content-type"]="application/json; charset=UTF-8"
        preferenceDataStore.getFromDataStore().onEach {  }.collect {
            requestHeaders["access-token"] = it.access_token
            requestHeaders["client"] = it.client
            requestHeaders["uid"] = it.uid
        }
    }
}