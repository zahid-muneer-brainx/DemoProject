package com.example.demoproject

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Meta (
    @SerializedName("current_page")
    val current_page:String,
    @SerializedName("total_pages")
    val total_pages:String
        ):Serializable