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
import com.kerberos.trackingSdk.ui.BottomNavigationBar
import com.kerberos.trackingSdk.ui.LiveTrackingScreen
import com.kerberos.trackingSdk.ui.MainScreen
import com.kerberos.trackingSdk.ui.theme.ui.theme.MyApplicationTheme
import com.kerberos.trackingSdk.ui.trip.TripMapScreen
import com.kerberos.trackingSdk.ui.trip.TripScreen
import com.kerberos.trackingSdk.ui.settings.SettingsScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }

}

