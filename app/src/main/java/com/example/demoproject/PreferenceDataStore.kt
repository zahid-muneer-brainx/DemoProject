package com.example.demoproject

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map


class PreferenceDataStore(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "USER_DATASTORE")

    companion object {

        val UID = stringPreferencesKey("uid")
        val ACCESSTOKEN = stringPreferencesKey("accesstoken")
        val CLIENT = stringPreferencesKey("client")
        val loginStatus= booleanPreferencesKey("loginstatus")
    }

    suspend fun savetoDataStore(requestHeadersModel: RequestHeadersModel) {
        context.dataStore.edit {

            it[UID] = requestHeadersModel.uid
            it[ACCESSTOKEN] = requestHeadersModel.access_token
            it[CLIENT] = requestHeadersModel.client

        }
    }

    fun getFromDataStore() = context.dataStore.data.map {
        RequestHeadersModel(
            uid = it[UID] ?: "",
            access_token = it[ACCESSTOKEN] ?: "",
            client = it[CLIENT] ?: ""
        )
    }
    suspend fun saveLoginStatus(status:Boolean)
    {
        context.dataStore.edit {

            it[loginStatus]=status

        }
    }
    suspend fun getLoginStatus()= context.dataStore.data.map {
           it[loginStatus] ?: false
    }


}
