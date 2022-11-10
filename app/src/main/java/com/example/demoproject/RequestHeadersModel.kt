package com.example.demoproject

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RequestHeadersModel(
    @SerializedName("Content-type")
    val ContentType:String,
    @SerializedName("uid")
    val uid: String,
    @SerializedName("access-token")
    val access_token: String,
    @SerializedName("client")
    val client: String

) : Serializable {
}