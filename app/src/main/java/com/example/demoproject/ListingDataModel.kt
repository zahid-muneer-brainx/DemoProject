package com.example.demoproject

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ListingDataModel(
    @SerializedName("card_contacts")
    val cardContactModels: ArrayList<CardContactsModel>,
    @SerializedName("meta")
    val metaModel: MetaModel
) : Serializable
