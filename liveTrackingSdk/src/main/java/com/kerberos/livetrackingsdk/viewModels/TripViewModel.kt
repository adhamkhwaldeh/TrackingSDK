package com.kerberos.livetrackingsdk.viewModels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aljawad.sons.gorestrepository.repositories.TripPagingRepository
import com.kerberos.livetrackingsdk.factories.TripUseCaseFactory
import com.kerberos.livetrackingsdk.models.TripModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TripViewModel constructor(
    private val tripPagingRepository: TripPagingRepository,
    private val tripUseCaseFactory: TripUseCaseFactory,
    private val context: Context
) :
    ViewModel() {

    fun getTripList(): Flow<PagingData<TripModel>> {
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

}