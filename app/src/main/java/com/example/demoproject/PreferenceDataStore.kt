package com.example.demoproject

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Singleton


class PreferenceDataStore(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "USER_DATASTORE")

    companion object {

        val UID = stringPreferencesKey("uid")
        val ACCESSTOKEN = stringPreferencesKey("accesstoken")
        val CLIENT = stringPreferencesKey("client")

    }

    suspend fun savetoDataStore(requestHeaders: RequestHeaders) {
        context.dataStore.edit {

            it[UID] = requestHeaders.uid
            it[ACCESSTOKEN] = requestHeaders.access_token
            it[CLIENT] = requestHeaders.client

        }
    }

    fun getFromDataStore() = context.dataStore.data.map {
        RequestHeaders(
            uid = it[UID] ?: "",
            access_token = it[ACCESSTOKEN] ?: "",
            client = it[CLIENT] ?: ""
        )
    }
}
