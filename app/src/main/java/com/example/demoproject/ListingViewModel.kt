package com.example.demoproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class ListingViewModel @Inject constructor(private var listingRepository: ListingRepository) :
    ViewModel() {

    var recyclerDataArrayList = MutableLiveData<ListingDataModel>()
    var searchDataArrayList = MutableLiveData<ListingDataModel>()
    fun getList(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            listingRepository.collectFromDatastore()
            println("Job canceled....")
        }
        viewModelScope.launch(Dispatchers.IO) {
            listingRepository.getlist(recyclerDataArrayList, page)
            println("Inside getList function")
        }
    }

    fun searchByName(name: String?, page: Int) {
        val job=viewModelScope.launch(Dispatchers.IO) {
            listingRepository.collectFromDatastore()
            println("Job canceled....")
        }

        viewModelScope.launch(Dispatchers.IO) {
            listingRepository.searchByName(name, searchDataArrayList, page)
        }
    }
}
