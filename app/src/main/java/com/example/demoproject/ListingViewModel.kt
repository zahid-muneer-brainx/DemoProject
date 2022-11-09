package com.example.demoproject

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


@HiltViewModel
class ListingViewModel @Inject constructor(private var listingRepository: ListingRepository) :
    ViewModel() {

    var recyclerDataArrayList = MutableLiveData<ListingDataModel>()
    var searchDataArrayList = MutableLiveData<ListingDataModel>()
    fun getlist(page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            listingRepository.getlist(recyclerDataArrayList,page)
            println("API call.....")
        }
    }

    fun searchByName(name: String?,page:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            listingRepository.searchByName(name, searchDataArrayList,page)
        }
    }
}
