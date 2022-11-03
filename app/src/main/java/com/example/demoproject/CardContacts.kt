package com.example.demoproject

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CardContacts(
    @SerializedName("id")
    val id:Int,
    @SerializedName("full_name")
    val full_name:String,
    @SerializedName("phone_number")
    val phone_number:String,
    @SerializedName("email")
    val email:String,
    @SerializedName("date")
    val date:String,
    @SerializedName("created_at")
    val created_at:String
): Serializable
