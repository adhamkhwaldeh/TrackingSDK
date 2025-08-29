package com.kerberos.trackingSdk.base.stateLayout.defaultStates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.adham.gini.weatherginisdk.R
import com.adham.gini.weatherginisdk.ui.navigations.NavigationItem


/**
 * No internet connection compose
 *
 * @param retry
 * @receiver
 */
@Composable
fun NoInternetConnectionCompose(retry: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .fillMaxHeight()
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.noConnection),
            style = MaterialTheme.typography.labelLarge
        )
        OutlinedButton(
            modifier = Modifier.padding(8.dp),
            onClick = {
                retry()
            },
        ) {
            Text(text = stringResource(R.string.Retry))
        }
    }
}