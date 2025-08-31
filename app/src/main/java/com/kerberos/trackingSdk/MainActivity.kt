package com.kerberos.trackingSdk

import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kerberos.livetrackingsdk.LiveTrackingManager
import com.kerberos.livetrackingsdk.enums.LiveTrackingMode
import com.kerberos.livetrackingsdk.enums.TrackingState
import com.kerberos.livetrackingsdk.interfaces.ITrackingLocationListener
import com.kerberos.livetrackingsdk.interfaces.ITrackingStatusListener
import com.kerberos.trackingSdk.viewModels.TripStatus
import com.kerberos.trackingSdk.ui.BottomNavigationBar
import com.kerberos.trackingSdk.ui.theme.ui.theme.MyApplicationTheme
import com.kerberos.trackingSdk.ui.trip.TripMapScreen
import com.kerberos.trackingSdk.ui.trip.TripScreen
import com.kerberos.trackingSdk.ui.settings.SettingsScreen
import com.kerberos.trackingSdk.ui.trip.TripControls
import kotlinx.coroutines.DelicateCoroutinesApi
import timber.log.Timber

@OptIn(DelicateCoroutinesApi::class)
class MainActivity : ComponentActivity() {

    private val liveTrackingManager: LiveTrackingManager by lazy {
        LiveTrackingManager.Builder(this)
            .setLocationUpdateInterval(1000L)
            .setMinDistanceMeters(5f)
            .setBackgroundService(TripBackgroundService::class.java)
            .setLiveTrackingMode(LiveTrackingMode.BACKGROUND_SERVICE)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TripControls(
                    tripStatus = TripStatus.RUNNING,
                    onStart = {
                        liveTrackingManager.onStartTracking()
                    },
                    onPause = {
                        liveTrackingManager.onPauseTracking()
                    },
                    onResume = {
                        liveTrackingManager.onResumeTracking()
                    },
                    onStop = {
                        liveTrackingManager.onStopTracking()
                    }
                )
                MainScreen()
            }
        }
        liveTrackingManager.currentTrackingManager.initializeTrackingManager()
        liveTrackingManager.onStartTracking()

        liveTrackingManager.addTrackingLocationListener(listener = object :
            ITrackingLocationListener {
            override fun onLocationUpdated(currentLocation: Location?) {
                Timber.d("onLocationUpdated")
            }

            override fun onLocationUpdateFailed(exception: Exception) {
                Timber.d("onLocationUpdateFailed")
            }
        })

        liveTrackingManager.addTrackingStatusListener(listener = object :
            ITrackingStatusListener {
            override fun onTrackingStateChanged(trackingState: TrackingState) {
                Timber.d("onTrackingStateChanged")
            }
        })
    }

}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "List",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("Map") { TripMapScreen() }
            composable("List") { TripScreen() }
            composable("Settings") { SettingsScreen() }
        }
    }
}