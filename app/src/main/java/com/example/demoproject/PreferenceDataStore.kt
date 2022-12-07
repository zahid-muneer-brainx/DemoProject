package com.example.demoproject

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach


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
            client = it[CLIENT] ?: "",
            ContentType = "application/json; charset=UTF-8"
        )
    }.onEach { }.flowOn(Dispatchers.Default)
    suspend fun saveLoginStatus(status:Boolean)
    {
        context.dataStore.edit {

            it[loginStatus]=status

        }
    }
    fun getLoginStatus()= context.dataStore.data.map {
           it[loginStatus] ?: false
    }


}
