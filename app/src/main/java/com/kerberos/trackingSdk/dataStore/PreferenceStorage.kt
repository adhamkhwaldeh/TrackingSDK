package com.kerberos.trackingSdk.dataStore

import com.kerberos.livetrackingsdk.models.SdkSettings
import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {

    val trackSDKConfiguration: Flow<SdkSettings?>
    suspend fun setTrackSDKConfiguration(configuration: SdkSettings)

    /***
     * clears all the stored data
     */

    suspend fun clearPreferenceStorage()
}