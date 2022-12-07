package com.example.demoproject

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateProfileInfoModel(
    @SerializedName("personal_number")
    val personalNumber: String,
    @SerializedName("profile_picture")
    val profilePic: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("device_token")
    val device_token: String,
    @SerializedName("app_platform")
    val app_platform: String,
    @SerializedName("call_forwarding")
    val call_forwarding: Boolean,
    @SerializedName("app_version")
    val app_version: Int

) : Serializable
