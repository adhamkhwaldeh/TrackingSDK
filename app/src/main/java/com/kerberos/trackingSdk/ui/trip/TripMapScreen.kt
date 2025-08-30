package com.kerberos.trackingSdk.ui.trip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.kerberos.livetrackingsdk.viewModels.TripTrackViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TripMapScreen(viewModel: TripTrackViewModel = koinViewModel()) {
    val tripTracks by viewModel.tripTracks.collectAsState()
    val tripStatus by viewModel.tripStatus.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 10f)
    }

    LaunchedEffect(Unit) {
        viewModel.getTripTracks(1) // todo: pass trip id
    }

    LaunchedEffect(tripTracks) {
        if (tripTracks.isNotEmpty()) {
            val firstTrack = tripTracks.first()
            cameraPositionState.position =
                CameraPosition.fromLatLngZoom(
                    LatLng(firstTrack.latitude, firstTrack.longitude),
                    15f
                )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            cameraPositionState = cameraPositionState
        ) {
            if (tripTracks.size > 1) {
                Polyline(points = tripTracks.map { LatLng(it.latitude, it.longitude) })
            }
        }

        TripControls(
            tripStatus = tripStatus,
            onStart = viewModel::startTrip,
            onPause = viewModel::pauseTrip,
            onResume = viewModel::resumeTrip,
            onStop = viewModel::stopTrip,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }

}
