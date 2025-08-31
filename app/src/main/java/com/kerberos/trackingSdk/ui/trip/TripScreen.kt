package com.kerberos.trackingSdk.ui.trip

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.kerberos.trackingSdk.viewModels.TripViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TripScreen(viewModel: TripViewModel = koinViewModel()) {
    val lazyTripItems = viewModel.getTripList().collectAsLazyPagingItems()
    val context = LocalContext.current

    val importCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.importTripsCsv(uri)
            }
        }
    }

    val exportCsvLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.exportTripsCsv(uri)
            }
        }
    }

    val importJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.importTripsJson(uri)
            }
        }
    }

    val exportJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.exportTripsJson(uri)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "text/csv"
                }
                importCsvLauncher.launch(intent)
            }) {
                Text(text = "Import CSV")
            }
            Button(onClick = {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "text/csv"
                    putExtra(Intent.EXTRA_TITLE, "trips.csv")
                }
                exportCsvLauncher.launch(intent)
            }) {
                Text(text = "Export CSV")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/json"
                }
                importJsonLauncher.launch(intent)
            }) {
                Text(text = "Import JSON")
            }
            Button(onClick = {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/json"
                    putExtra(Intent.EXTRA_TITLE, "trips.json")
                }
                exportJsonLauncher.launch(intent)
            }) {
                Text(text = "Export JSON")
            }
        }

        Box(modifier = Modifier.weight(1f)) {
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

            // Handle load states: initial loading, error, empty
            val refreshState = lazyTripItems.loadState.refresh
            val appendState = lazyTripItems.loadState.append
            if (refreshState is LoadState.Loading && lazyTripItems.itemCount == 0) {
                // initial loading
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (refreshState is LoadState.Error && lazyTripItems.itemCount == 0) {
                val error = (refreshState as LoadState.Error).error
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Error: ${error.message ?: "Unknown"}")
                    Button(onClick = { lazyTripItems.retry() }) {
                        Text(text = "Retry")
                    }
                }
            } else if (appendState is LoadState.Error) {
                // show small retry when appending pages fail
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Failed to load more: ${(appendState as LoadState.Error).error.message ?: "Unknown"}")
                    Button(onClick = { lazyTripItems.retry() }) {
                        Text(text = "Retry")
                    }
                }
            } else if (lazyTripItems.loadState.append.endOfPaginationReached && lazyTripItems.itemCount == 0) {
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

//            lazyTripItems.loadState.let { loadState ->
//                if (loadState.refresh is LoadState.Loading) {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//                }
//                if (loadState.refresh is LoadState.Error) {
//                    val error = (loadState.refresh as LoadState.Error).error
//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(text = "Error: ${error.message}")
//                        Button(onClick = { lazyTripItems.retry() }) {
//                            Text(text = "Retry")
//                        }
//                    }
//                }
//                if (loadState.append.endOfPaginationReached && lazyTripItems.itemCount == 0) {
//                    Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(text = "No trips found.")
//                    }
//                }
//            }
//        }
//    }
//}
