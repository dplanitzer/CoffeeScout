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

import android.Manifest
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.example.coffeescout.repository.Business
import com.example.coffeescout.repository.BusinessAddress
import com.example.coffeescout.repository.BusinessesRepository
import com.example.coffeescout.repository.createBusinessRepository
import com.example.coffeescout.ui.theme.AppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    companion object {
        const val BUSINESS_CATEGORY = "coffee"
        const val BUSINESS_SORTING = "distance"
        const val INITIAL_LOAD_SIZE = 10
        const val LOAD_SIZE = 10

        const val YELP_URL = "https://www.yelp.com"
    }


    private val locationPermissions = setOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val requestMultiplePermissions = RequestMultiplePermissions(this)

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var repository: BusinessesRepository

    private lateinit var businessFormatter: BusinessFormatter

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(
        repository,
        getString(R.string.nearby_address),
        this::onResolveAddress,
        BUSINESS_CATEGORY,
        BUSINESS_SORTING,
        INITIAL_LOAD_SIZE,
        LOAD_SIZE) }


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        businessFormatter = BusinessFormatter(resources)
        repository = createBusinessRepository()


        setContent {
            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { contentPadding ->
                    MainScreen(
                        modifier = Modifier.padding(contentPadding),
                        initialStreetAddress = stringResource(R.string.nearby_address),
                        businessFormatter = businessFormatter,
                        viewModel = viewModel,
                        onAction = this::onBusinessCardAction,
                        onAddressChange = this::onAddressChange
                    )
                }
            }
        }
    }

    // Handles the various user actions of a business card
    private fun onBusinessCardAction(action: BusinessCardAction, b: Business) {

        when (action) {
            BusinessCardAction.GoToDetails -> {
                openUrl(b.detailsPageUrl)
            }

            BusinessCardAction.GoToReviews -> {
                openUrl(b.reviewsUrl)
            }

            BusinessCardAction.GoToNavigation -> {
                if (b.geoLocation != null) {
                    openTurnByTurnNavigationTo(b.geoLocation)
                }
            }

            BusinessCardAction.GoToYelp -> {
                openUrl(YELP_URL)
            }
        }
    }

    // Handles the case when the user enters a new address in the address input bar.
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private fun onAddressChange(newAddress: String, lazyPagingItems: LazyPagingItems<Business>) {
        viewModel.address = newAddress
        lazyPagingItems.refresh()
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private suspend fun onResolveAddress(newAddress: String): BusinessAddress {
        val addr = newAddress.trim()

        return if (getString(R.string.nearby_address).equals(addr, ignoreCase = true)) {
            val (granted, denied) = requestMultiplePermissions.request(locationPermissions)

            if (!granted.contains(Manifest.permission.ACCESS_FINE_LOCATION)
                && !granted.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                throw LocationPermissionDenied()
            }


            val location = withContext(Dispatchers.IO) { await(fusedLocationClient.lastLocation) }
            BusinessAddress.Location(location.latitude, location.longitude)
        } else {
            BusinessAddress.Address(addr)
        }
    }
}

class LocationPermissionDenied : Exception("")
