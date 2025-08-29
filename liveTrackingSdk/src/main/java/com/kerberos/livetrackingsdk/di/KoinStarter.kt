package com.kerberos.livetrackingsdk.di

import android.app.Application
import com.aljawad.sons.gorestrepository.repositories.TripPagingRepository
import com.kerberos.livetrackingsdk.orm.LiveTrackingDatabase
import com.kerberos.livetrackingsdk.repositories.repositories.TripPagingRepositoryImpl
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
                    viewModelModule
                )
            )
        }
    }

    private val viewModelModule = module {
        viewModel { TripViewModel(get()) }
    }

    private val repositoryModule = module {
        single<TripPagingRepository> { TripPagingRepositoryImpl(get()) }
    }

    private val databaseModule = module {
        single { LiveTrackingDatabase.getDatabase(get()).tripDao() }
    }

}