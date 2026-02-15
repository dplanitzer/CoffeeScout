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

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coffeescout.repository.Business
import com.example.coffeescout.repository.BusinessAddress
import com.example.coffeescout.repository.BusinessesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max

class BusinessesDataSource(
    private val repository: BusinessesRepository,
    private val address: String,
    private val onResolveAddress: suspend (String) -> BusinessAddress,
    private val category: String,
    private val sortBy: String
    ) : PagingSource<Int, Business>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Business> {

        return try {
            val addr = withContext(Dispatchers.Main) {
                onResolveAddress(address)
            }

            val offset = params.key ?: 0
            val qr = repository.query(addr, category, sortBy, offset, params.loadSize)
            val prevKey = if (offset > 0) max(offset - params.loadSize, 0) else null
            val nextKey = if (offset + qr.businesses.size < qr.totalCount) offset + qr.businesses.size else null

            LoadResult.Page(
                data = qr.businesses,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (ex: Exception) {
            Log.e(TAG, "load: ${ex.message}")
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Business>): Int? {
        return state.anchorPosition
    }


    companion object {
        private const val TAG = "BusinessDataSource"
    }
}
