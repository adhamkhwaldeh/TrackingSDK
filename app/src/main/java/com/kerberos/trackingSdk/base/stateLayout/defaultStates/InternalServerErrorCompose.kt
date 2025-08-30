package com.kerberos.trackingSdk.base.stateLayout.defaultStates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kerberos.trackingSdk.R


/**
 * Not authorized compose
 *
 */
@Composable
fun InternalServerErrorCompose(message: String?) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message ?: stringResource(R.string.internalServerError),
            style = MaterialTheme.typography.labelLarge
        )
    }
}


