package com.kerberos.trackingSdk

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
import com.kerberos.livetrackingsdk.trackingManagers.BackgroundTrackingManager
import com.kerberos.trackingSdk.viewModels.TripStatus
import com.kerberos.trackingSdk.ui.BottomNavigationBar
import com.kerberos.trackingSdk.ui.theme.ui.theme.MyApplicationTheme
import com.kerberos.trackingSdk.ui.trip.TripMapScreen
import com.kerberos.trackingSdk.ui.trip.TripScreen
import com.kerberos.trackingSdk.ui.settings.SettingsScreen
import com.kerberos.trackingSdk.ui.trip.TripControls
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
class MainActivity : ComponentActivity() {

    private val trackServiceUtils: BackgroundTrackingManager by lazy {
        BackgroundTrackingManager(TripBackgroundService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TripControls(
                    tripStatus = TripStatus.RUNNING,
                    onStart = {
                        trackServiceUtils.itsTrackService?.startTracking()
                    },
                    onPause = {
                        trackServiceUtils.itsTrackService?.pauseTracking()
                    },
                    onResume = {
                        trackServiceUtils.itsTrackService?.resumeTracking()
                    },
                    onStop = {
                        trackServiceUtils.itsTrackService?.stopTracking()
                    }
                )
//                MainScreen()
            }
        }
        trackServiceUtils.bind(this)
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