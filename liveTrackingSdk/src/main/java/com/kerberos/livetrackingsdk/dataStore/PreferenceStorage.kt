package com.kerberos.livetrackingsdk.dataStore

import com.kerberos.livetrackingsdk.models.TrackSDKConfigurationModel

import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {

    val trackSDKConfiguration: Flow<TrackSDKConfigurationModel?>
    suspend fun setTrackSDKConfiguration(configuration: TrackSDKConfigurationModel)

    /***
     * clears all the stored data
     */

    suspend fun clearPreferenceStorage()
}