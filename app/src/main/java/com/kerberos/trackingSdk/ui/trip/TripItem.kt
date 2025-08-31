package com.kerberos.trackingSdk.ui.trip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kerberos.trackingSdk.models.TripModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TripItem(trip: TripModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Trip ID: ${trip.id}", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Start Time: ", style = MaterialTheme.typography.bodyMedium)
                Text(text = trip.startTime.toFormattedDate(), style = MaterialTheme.typography.bodyMedium)
            }
            if (trip.endTime != null) {
                Row {
                    Text(text = "End Time: ", style = MaterialTheme.typography.bodyMedium)
                    Text(text = trip.endTime!!.toFormattedDate(), style = MaterialTheme.typography.bodyMedium)
                }
            }
            if (trip.tripDuration != null) {
                Row {
                    Text(text = "Duration: ", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "${trip.tripDuration} seconds", style = MaterialTheme.typography.bodyMedium)
                }
            }
            if (trip.totalDistance != null) {
                Row {
                    Text(text = "Distance: ", style = MaterialTheme.typography.bodyMedium)
                    Text(text = String.format("%.2f km", trip.totalDistance), style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (trip.isActive) "Status: Active" else "Status: Inactive",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun Long.toFormattedDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
}
