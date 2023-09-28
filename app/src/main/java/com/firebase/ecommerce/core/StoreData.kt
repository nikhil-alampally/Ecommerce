package com.firebase.ecommerce.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoreData @Inject constructor( private val dataStore: DataStore<Preferences>) {


    companion object {
       /* private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storeData")*/
        val USERNAME = stringPreferencesKey("store_Data")

    }


    val getData: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[USERNAME] ?: ""
        }

    suspend fun saveData(name: String) {
       dataStore.edit{preferences ->
            preferences[USERNAME]=name
        }
    }

}