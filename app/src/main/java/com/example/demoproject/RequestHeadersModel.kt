package com.example.demoproject

import com.google.gson.annotations.SerializedName
import okhttp3.Request
import okhttp3.Response
import retrofit2.http.Headers
import java.io.Serializable

data class RequestHeadersModel(
    @SerializedName("Content-type")
    var ContentType:String,
    @SerializedName("uid")
    var uid: String,
    @SerializedName("access-token")
    var access_token: String,
    @SerializedName("client")
    var client: String

) : Serializable {
    fun setRequestHeaders(headers: okhttp3.Headers)
    {
        this.ContentType="application/json; charset=UTF-8"
        this.uid=headers.get("uid").toString()
        this.access_token=headers.get("access-token").toString()
        this.client=headers.get("client").toString()
    }
}