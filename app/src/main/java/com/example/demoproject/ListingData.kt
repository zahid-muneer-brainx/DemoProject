package com.example.demoproject

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ListingData(
    @SerializedName("card_contacts")
    val cardContacts: CardContacts,
    @SerializedName("meta")
    val meta: Meta
):Serializable
