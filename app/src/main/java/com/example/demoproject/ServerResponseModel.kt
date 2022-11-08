package com.example.demoproject

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ServerResponseModel(
    @SerializedName("name")
    val name: String

) : Serializable
