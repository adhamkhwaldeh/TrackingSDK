package com.kerberos.trackingSdk.viewModels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kerberos.trackingSdk.repositories.repositories.TripPagingRepository
import com.kerberos.trackingSdk.factories.TripUseCaseFactory
import com.kerberos.trackingSdk.mappers.toTrip
import com.kerberos.trackingSdk.models.TripModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TripViewModel constructor(
    private val tripPagingRepository: com.kerberos.trackingSdk.repositories.repositories.TripPagingRepository,
    private val tripUseCaseFactory: com.kerberos.trackingSdk.factories.TripUseCaseFactory,
    private val context: Context
) :
    ViewModel() {

    fun getTripList(): Flow<PagingData<com.kerberos.trackingSdk.models.TripModel>> {
        return tripPagingRepository.getTripPageList(1).cachedIn(viewModelScope)
    }

    fun importTripsCsv(uri: Uri) {
        viewModelScope.launch {
            val useCase = tripUseCaseFactory.createImportUseCase("csv")
            context.contentResolver.openInputStream(uri)?.let {
                useCase.execute(it)
            }
        }
    }

    fun exportTripsCsv(uri: Uri) {
        viewModelScope.launch {
            val useCase = tripUseCaseFactory.createExportUseCase("csv")
            context.contentResolver.openOutputStream(uri)?.let {
                useCase.execute(it)
            }
        }
    }

    fun importTripsJson(uri: Uri) {
        viewModelScope.launch {
            val useCase = tripUseCaseFactory.createImportUseCase("json")
            context.contentResolver.openInputStream(uri)?.let {
                useCase.execute(it)
            }
        }
    }

    fun exportTripsJson(uri: Uri) {
        viewModelScope.launch {
            val useCase = tripUseCaseFactory.createExportUseCase("json")
            context.contentResolver.openOutputStream(uri)?.let {
                useCase.execute(it)
            }
        }
    }

    fun updateTrip(tripModel: com.kerberos.trackingSdk.models.TripModel) {
        viewModelScope.launch {
            val useCase = tripUseCaseFactory.createUpdateUseCase()
            useCase.execute(tripModel.toTrip())
        }
    }

    fun deleteTrip(tripModel: com.kerberos.trackingSdk.models.TripModel) {
        viewModelScope.launch {
            val useCase = tripUseCaseFactory.createDeleteUseCase()
            useCase.execute(tripModel.toTrip())
        }
    }

}