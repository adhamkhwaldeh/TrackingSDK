package com.kerberos.trackingSdk.ui.trip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kerberos.livetrackingsdk.viewModels.TripViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TripScreen(viewModel: TripViewModel = koinViewModel()) {
    val lazyTripItems = viewModel.getTripList().collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(lazyTripItems.itemCount) { index ->
                lazyTripItems[index]?.let { trip ->
                    TripItem(trip = trip)
                }
            }
        }

        lazyTripItems.loadState.let { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            if (loadState.refresh is LoadState.Error) {
                val error = (loadState.refresh as LoadState.Error).error
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Error: ${error.message}")
                    Button(onClick = { lazyTripItems.retry() }) {
                        Text(text = "Retry")
                    }
                }
            }
            if (loadState.append.endOfPaginationReached && lazyTripItems.itemCount == 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No trips found.")
                }
            }
        }
    }
}
