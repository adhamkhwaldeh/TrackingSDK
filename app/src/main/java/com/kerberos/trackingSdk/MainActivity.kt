package com.kerberos.trackingSdk

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.kerberos.trackingSdk.ui.MainScreen
import com.kerberos.trackingSdk.ui.theme.ui.theme.MyApplicationTheme


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val theme by appPrefsStorage.getThemeFlow().collectAsState(initial = "Light")

            LaunchedEffect(theme) {
                val mode = when (theme) {
                    "Dark" -> AppCompatDelegate.MODE_NIGHT_YES
                    "Light" -> AppCompatDelegate.MODE_NIGHT_NO
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
                AppCompatDelegate.setDefaultNightMode(mode)
            }


            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}

