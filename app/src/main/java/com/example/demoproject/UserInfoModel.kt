package com.example.demoproject

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.inject.Inject

data class UserInfoModel(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
) : Serializable
