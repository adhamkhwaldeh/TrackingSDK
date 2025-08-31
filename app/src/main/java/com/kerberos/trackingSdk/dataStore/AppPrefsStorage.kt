package com.kerberos.trackingSdk.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.google.gson.Gson
import com.kerberos.livetrackingsdk.models.TrackSDKConfigurationModel

import kotlinx.coroutines.flow.*
import java.io.IOException

class AppPrefsStorage(var context: Context) : PreferenceStorage {

    private val Context.dataStore by preferencesDataStore("AppPrefStorage")

    //region Logged user
    override val trackSDKConfiguration: Flow<TrackSDKConfigurationModel?>
        get() = context.dataStore.getValueAsFlow(PreferencesKeys.TRACK_SDK_CONFIGURATION, "")
            .map { preferences ->
                if (!preferences.isNullOrBlank()) Gson().fromJson(
                    preferences,
                    TrackSDKConfigurationModel::class.java
                ) else null
            }

    override suspend fun setTrackSDKConfiguration(loggedUser: TrackSDKConfigurationModel) {
        context.dataStore.setValue(
            PreferencesKeys.TRACK_SDK_CONFIGURATION,
            Gson().toJson(loggedUser)
        )
    }
    //endregion

    /***
     * clears all the stored data
     */
    override suspend fun clearPreferenceStorage() {
        context.dataStore.edit {
            it.clear()
        }
    }

    /***
     * handy function to save key-value pairs in Preference. Sets or updates the value in Preference
     * @param key used to identify the preference
     * @param value the value to be saved in the preference
     */
    private suspend fun <T> DataStore<Preferences>.setValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        this.edit { preferences ->
            // save the value in prefs
            preferences[key] = value
        }
    }

    /***
     * handy function to return Preference value based on the Preference key
     * @param key  used to identify the preference
     * @param defaultValue value in case the Preference does not exists
     * @throws Exception if there is some error in getting the value
     * @return [Flow] of [T]
     */
    private fun <T> DataStore<Preferences>.getValueAsFlow(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> {
        return this.data.catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                // we try again to store the value in the map operator
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            // return the default value if it doesn't exist in the storage
            preferences[key] ?: defaultValue
        }
    }

}