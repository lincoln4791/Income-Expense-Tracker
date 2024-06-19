package com.lincoln4791.dailyexpensemanager.common

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject




private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_data_store")

class UserPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val appContext = context.applicationContext

    val accessToken: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }

    val refreshToken: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }

    val name: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[NAME]
        }


    val phone: Flow<String?>
        get() = appContext.dataStore.data.map { preferences ->
            preferences[PHONE]
        }


    suspend fun saveName(name : String) {
        appContext.dataStore.edit { preferences ->
            preferences[NAME] = name
        }
    }

    suspend fun savePhone(name : String) {
        appContext.dataStore.edit { preferences ->
            preferences[PHONE] = name
        }
    }

    suspend fun saveAccessTokens(accessToken: String, refreshToken: String) {
        appContext.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun clear() {
        appContext.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun saveUserDetails(accessToken: String, refreshToken: String,name: String,phone:String){
        appContext.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
            preferences[NAME] = name
            preferences[PHONE] = phone
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("key_access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("key_refresh_token")
        private val NAME = stringPreferencesKey("name")
        private val PHONE = stringPreferencesKey("phone")
    }

}