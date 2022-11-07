package com.example.demoproject

import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

        @Headers("Content-type: application/json; charset=UTF-8")
        @POST("/api/v1/users/sign_in.json")
        fun login(@Body info: UserInfo): Call<ServerResponse?>
        @Headers("Content-type: application/json; charset=UTF-8")
        @GET("/api/v1/card_contacts")
        fun allContacts(@HeaderMap headers: Map<String, String>): Call<ListingData>
        @Headers("Content-type: application/json; charset=UTF-8")
        @PUT("/api/v1/users.json")
        fun update(@HeaderMap headers: Map<String, String>, @Body info: UpdateInfo):Call<ServerResponse?>
        @GET("/api/v1/card_contacts")
        fun search(@HeaderMap headers: Map<String, String>, @Query("query") name:String?): Call<ListingData>
}