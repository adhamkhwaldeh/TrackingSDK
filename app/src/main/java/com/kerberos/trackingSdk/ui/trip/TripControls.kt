package com.kerberos.trackingSdk.ui.trip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kerberos.livetrackingsdk.viewModels.TripStatus

@Composable
fun TripControls(
    tripStatus: TripStatus,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        when (tripStatus) {
            TripStatus.STOPPED -> {
                Button(onClick = onStart) {
                    Text("Start")
                }
            }
            TripStatus.RUNNING -> {
                Button(onClick = onPause) {
                    Text("Pause")
                }
                Button(onClick = onStop) {
                    Text("Stop")
                }
            }
            TripStatus.PAUSED -> {
                Button(onClick = onResume) {
                    Text("Resume")
                }
                Button(onClick = onStop) {
                    Text("Stop")
                }
            }
        }
    }
}