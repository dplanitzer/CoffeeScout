//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.coffeescout.repository.Business
import com.example.coffeescout.repository.BusinessesRepository

// A view model which stores the result of running a (paged) query for businesses:
// - notifies observers when new businesses are available
// - notifies observers when a query encounters an error. Eg network is down
class MainViewModel(
    repository: BusinessesRepository,
    userAddress: String,
    category: String,
    sortBy: String,
    initialLoadSize: Int,
    loadSize: Int
) : ViewModel() {

    // Observe this to get the businesses
    val liveData: LiveData<PagedList<Business>>

    // Observe this to get the current error state
    private val _liveError = MutableLiveData<Exception?>(null)
    val liveError: LiveData<Exception?> = _liveError


    init {

        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(loadSize)
            .setInitialLoadSizeHint(initialLoadSize)
            .setPageSize(loadSize)
            .build()

        val factory = BusinessesDataSourceFactory(repository, userAddress, category, sortBy)
        liveData = LivePagedListBuilder(factory, pagedListConfig)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<Business>() {
                override fun onZeroItemsLoaded() {
                    // If mostRecentError != null -> app will show an error view
                    // If mostRecentError == null -> app will show an empty recycler view (we legitimately didn't receive any data from the server)
                    _liveError.value = repository.mostRecentError
                }
            })
            .build()
    }

    // Reset the state of the view model. This throws all saved data away and restarts the data
    // stream from scratch.
    fun invalidate() {

        _liveError.value = null
        liveData.value?.dataSource?.invalidate()
    }
}


class MainViewModelFactory(
    private val repository: BusinessesRepository,
    private val userAddress: String,
    private val category: String,
    private val sortBy: String,
    private val initialLoadSize: Int,
    private val loadSize: Int
    ) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MainViewModel(repository, userAddress, category, sortBy, initialLoadSize, loadSize) as T
    }
}
