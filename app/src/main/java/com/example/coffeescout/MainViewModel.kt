//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.coffeescout.repository.BusinessesRepository

// A view model which stores the result of running a (paged) query for businesses:
// - notifies observers when new businesses are available
// - notifies observers when a query encounters an error. Eg network is down
class MainViewModel(
    private val repository: BusinessesRepository,
    var streetAddress: String,
    private val category: String,
    private val sortBy: String,
    initialLoadSize: Int,
    loadSize: Int
) : ViewModel() {

    fun updateStreetAddress(newValue: String) {
        streetAddress = newValue
    }

    // Observe this to get the businesses
    val businessFlow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(
            enablePlaceholders = false,
            pageSize = loadSize,
            prefetchDistance = loadSize,
            initialLoadSize = initialLoadSize
        ),
        pagingSourceFactory = { BusinessesDataSource(repository, streetAddress, category, sortBy) }
    ).flow
        .cachedIn(viewModelScope)
}


class MainViewModelFactory(
    private val repository: BusinessesRepository,
    private val streetAddress: String,
    private val category: String,
    private val sortBy: String,
    private val initialLoadSize: Int,
    private val loadSize: Int
    ) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MainViewModel(repository, streetAddress, category, sortBy, initialLoadSize, loadSize) as T
    }
}
