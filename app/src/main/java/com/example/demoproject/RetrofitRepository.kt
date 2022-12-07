package com.example.demoproject

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitRepository @Inject constructor(){
    val retrofit = Retrofit.Builder()
        .baseUrl("https://staging.clientdex.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun getretrofitApi():RetrofitInterface
    {
       return retrofit.create(RetrofitInterface::class.java)
    }


}