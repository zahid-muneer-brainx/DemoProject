package com.example.demoproject


interface DatastoreRepo {
    suspend fun putString(key:String,value:String)
    suspend fun getString(key: String):String?
    suspend fun clearPReferences(key: String)
}