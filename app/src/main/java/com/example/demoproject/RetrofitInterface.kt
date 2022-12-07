package com.example.demoproject

import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {


    @POST("/api/v1/users/sign_in.json")
    fun login(@Body info: UserInfoModel): Call<ServerResponseModel?>


    @GET("/api/v1/card_contacts")
    fun allContacts(@HeaderMap headers: Map<String, String>,@Query("current_page") pageIndex:Int): Call<ListingDataModel>


    @PUT("/api/v1/users.json")
    fun update(
        @HeaderMap headers: Map<String, String>,
        @Body info: UpdateProfileInfoModel
    ): Call<ServerResponseModel?>

    @GET("/api/v1/card_contacts")
    fun search(
        @HeaderMap headers: Map<String, String>,
        @Query("query") name: String?,
        @Query("current_page") pageIndex:Int
    ): Call<ListingDataModel>
}