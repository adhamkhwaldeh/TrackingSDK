package com.kerberos.livetrackingsdk.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.koin.core.component.KoinComponent


/**
 * Base refactor view model
 *
 * @constructor
 *
 * @param androidApplication
 */
abstract class BaseRefactorViewModel(androidApplication: Application) :
    AndroidViewModel(androidApplication), KoinComponent {

}