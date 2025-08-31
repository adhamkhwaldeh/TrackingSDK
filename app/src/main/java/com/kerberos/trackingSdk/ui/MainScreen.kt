package com.kerberos.trackingSdk.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kerberos.trackingSdk.ui.settings.SettingsScreen
import com.kerberos.trackingSdk.ui.trip.TripMapScreen
import com.kerberos.trackingSdk.ui.trip.TripScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "Map",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("Map") { TripMapScreen() }
            composable("List") { TripScreen() }
            composable("Settings") { SettingsScreen() }
            composable("Live") { LiveTrackingScreen() }
        }
    }
}