package com.kerberos.livetrackingsdk.di

import android.app.Application
import com.aljawad.sons.gorestrepository.repositories.TripPagingRepository
import com.kerberos.livetrackingsdk.orm.LiveTrackingDatabase
import com.kerberos.livetrackingsdk.repositories.repositories.TripPagingRepositoryImpl
import com.kerberos.livetrackingsdk.repositories.repositories.TripTrackRepository
import com.aljawad.sons.gorestrepository.repositories.TripPagingRepository
import com.kerberos.livetrackingsdk.dataStore.AppPrefsStorage
import com.kerberos.livetrackingsdk.orm.LiveTrackingDatabase
import com.kerberos.livetrackingsdk.repositories.repositories.TripPagingRepositoryImpl
import com.kerberos.livetrackingsdk.repositories.repositories.TripTrackRepository
import com.kerberos.livetrackingsdk.ui.settings.SettingsViewModel
import com.kerberos.livetrackingsdk.viewModels.TripTrackViewModel
import com.kerberos.livetrackingsdk.viewModels.TripViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

object KoinStarter {

    fun startKoin(app: Application) {
        startKoin {
            androidLogger()
            androidContext(app)
            modules(
                listOf(
                    databaseModule,
                    repositoryModule,
                    viewModelModule,
                    storageModule
                )
            )
        }
    }

    private val viewModelModule = module {
        viewModel { TripTrackViewModel(get()) }
        viewModel { TripViewModel(get()) }
        viewModel { SettingsViewModel(get()) }
    }

    private val repositoryModule = module {
        single { TripTrackRepository(get()) }
        single<TripPagingRepository> { TripPagingRepositoryImpl(get()) }
    }

    private val databaseModule = module {
        single { LiveTrackingDatabase.getDatabase(get()).tripTrackDao() }
        single { LiveTrackingDatabase.getDatabase(get()).tripDao() }
    }

    private val storageModule = module {
        single { AppPrefsStorage(get()) }
    }

}