// Copyright (c) 2025 - 2026 Dietmar Planitzer <https://www.linkedin.com/in/dplanitzer/>
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
package com.example.coffeescout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.coffeescout.repository.BusinessAddress
import com.example.coffeescout.repository.BusinessesRepository

// A view model which stores the result of running a (paged) query for businesses:
// - notifies observers when new businesses are available
// - notifies observers when a query encounters an error. Eg network is down
class MainViewModel(
    private val repository: BusinessesRepository,
    var address: String,
    val onResolveAddress: suspend (String) -> BusinessAddress,
    private val category: String,
    private val sortBy: String,
    initialLoadSize: Int,
    loadSize: Int
) : ViewModel() {

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
        pagingSourceFactory = { BusinessesDataSource(repository, address, onResolveAddress, category, sortBy) }
    ).flow
        .cachedIn(viewModelScope)
}


class MainViewModelFactory(
    private val repository: BusinessesRepository,
    private val address: String,
    private val onResolveAddress: suspend (String) -> BusinessAddress,
    private val category: String,
    private val sortBy: String,
    private val initialLoadSize: Int,
    private val loadSize: Int
    ) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MainViewModel(repository, address, onResolveAddress, category, sortBy, initialLoadSize, loadSize) as T
    }
}
