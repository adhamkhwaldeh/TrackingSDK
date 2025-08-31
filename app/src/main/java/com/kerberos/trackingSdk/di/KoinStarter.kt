package com.kerberos.trackingSdk.di

import android.app.Application
import com.kerberos.trackingSdk.repositories.repositories.TripPagingRepository
import com.google.gson.Gson
import com.kerberos.trackingSdk.factories.TripUseCaseFactory
import com.kerberos.trackingSdk.orm.LiveTrackingDatabase
import com.kerberos.trackingSdk.repositories.repositories.TripPagingRepositoryImpl
import com.kerberos.trackingSdk.repositories.repositories.TripRepository
import com.kerberos.trackingSdk.repositories.repositories.TripTrackRepository
import com.kerberos.trackingSdk.viewModels.LiveTrackingViewModel
import com.kerberos.trackingSdk.viewModels.SettingsViewModel
import com.kerberos.trackingSdk.viewModels.TripTrackViewModel
import com.kerberos.trackingSdk.viewModels.TripViewModel
import com.kerberos.livetrackingsdk.managers.LocationTrackingManager
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
                    factoryModule,
                    serializationModule,
                    storageModule,
                    managerModule
                )
            )
        }
    }

    private val managerModule = module {
        single { LocationTrackingManager(get()) }
    }

    private val viewModelModule = module {
        viewModel { TripTrackViewModel(get(), get()) }
        viewModel { TripViewModel(get(), get(), get()) }
        viewModel { SettingsViewModel(get()) }
        viewModel { LiveTrackingViewModel(get()) }
    }

    private val repositoryModule = module {
        single { TripTrackRepository(get()) }
        single<TripPagingRepository> { TripPagingRepositoryImpl(get()) }
        single { TripRepository(get()) }
    }

    private val databaseModule = module {
        single { LiveTrackingDatabase.getDatabase(get()).tripTrackDao() }
        single { LiveTrackingDatabase.getDatabase(get()).tripDao() }
    }

    private val factoryModule = module {
        single { TripUseCaseFactory(get(), get()) }
    }

    private val serializationModule = module {
        single { Gson() }
    }
    private val storageModule = module {
        single { com.kerberos.trackingSdk.dataStore.AppPrefsStorage(get()) }
    }

}