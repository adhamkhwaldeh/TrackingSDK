package com.kerberos.trackingSdk

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import com.kerberos.trackingSdk.dataStore.AppPrefsStorage
import kotlinx.coroutines.runBlocking
import java.util.Locale

abstract class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    private fun updateBaseContextLocale(context: Context): Context {
        val language = runBlocking { AppPrefsStorage(context).getLanguage() }
        val locale = if (language == "Spanish") {
            Locale("es")
        } else {
            Locale.ENGLISH
        }
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
