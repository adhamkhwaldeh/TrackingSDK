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
import com.kerberos.livetrackingsdk.interfaces.ITrackingLocationInterface
import com.kerberos.livetrackingsdk.trackingManagers.BackgroundTrackingManager
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
        LiveTrackingManager(
            this,
            TripBackgroundService::class.java,
            LiveTrackingMode.BACKGROUND_SERVICE,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TripControls(
                    tripStatus = TripStatus.RUNNING,
                    onStart = {
                        liveTrackingManager.currentTrackingManager.onStartTracking()
                    },
                    onPause = {
                        liveTrackingManager.currentTrackingManager.onPauseTracking()
                    },
                    onResume = {
                        liveTrackingManager.currentTrackingManager.onResumeTracking()
                    },
                    onStop = {
                        liveTrackingManager.currentTrackingManager.onStopTracking()
                    }
                )
//                MainScreen()
            }
        }
        liveTrackingManager.currentTrackingManager.initializeTrackingManager()

        liveTrackingManager.currentTrackingManager.onStartTracking()

        liveTrackingManager.addTrackingLocationListener(listener = object :
            ITrackingLocationInterface {
            override fun onLocationUpdated(currentLocation: Location?) {
                Timber.d("onLocationUpdated")
            }

            override fun onLocationUpdateFailed(exception: Exception) {
                Timber.d("onLocationUpdateFailed")
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
            composable("List") { TripScreen() }
            composable("Map") { TripMapScreen() }
            composable("Settings") { SettingsScreen() }
        }
    }
}