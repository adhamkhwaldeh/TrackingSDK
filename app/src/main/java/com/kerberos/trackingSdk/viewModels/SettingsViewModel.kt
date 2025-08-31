package com.kerberos.trackingSdk.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kerberos.livetrackingsdk.models.SdkSettings
import com.kerberos.trackingSdk.dataStore.AppPrefsStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(private val appPrefsStorage: AppPrefsStorage) :
    ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val config = appPrefsStorage.trackSDKConfiguration.first()
            _uiState.value = SettingsUiState(
                locationUpdateInterval = config?.locationUpdateInterval?.toString() ?: "10000",
                backgroundTrackingEnabled = config?.backgroundTrackingToggle ?: false
            )
        }
    }

    fun onLocationUpdateIntervalChanged(interval: String) {
        _uiState.value = _uiState.value.copy(locationUpdateInterval = interval)
    }

    fun onBackgroundTrackingChanged(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(backgroundTrackingEnabled = enabled)
    }

    fun saveSettings() {
        viewModelScope.launch {
            val currentConfig = SdkSettings(
                locationUpdateInterval = _uiState.value.locationUpdateInterval.toLongOrNull()
                    ?: 10000L,
                backgroundTrackingToggle = _uiState.value.backgroundTrackingEnabled,
                minDistanceMeters = 25f
            )
            appPrefsStorage.setTrackSDKConfiguration(currentConfig)
        }
    }
}

data class SettingsUiState(
    val locationUpdateInterval: String = "10000",
    val backgroundTrackingEnabled: Boolean = false
)