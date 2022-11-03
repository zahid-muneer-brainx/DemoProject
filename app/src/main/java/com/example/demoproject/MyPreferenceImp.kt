package com.example.demoproject

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name="MyUsers")
@Singleton
class MyPreferenceImp @Inject constructor(
    private val context: Context
): DatastoreRepo {
    override suspend fun putString(key: String, value: String) {
        val prefereneKay = stringPreferencesKey(key)
        context.dataStore.edit {
            it[prefereneKay] = value
        }
    }


    override suspend fun getString(key: String): String? {
        return  try {
            val preferenceKey = stringPreferencesKey(key)
            val preference = context.dataStore.data.first()
            preference[preferenceKey]
        }catch (e:Exception){
            e.printStackTrace()
            null
        }
    }

    override suspend fun clearPReferences(key: String) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit {
            if (it.contains(preferenceKey)){
                it.remove(preferenceKey)
            }
        }
    }


}
