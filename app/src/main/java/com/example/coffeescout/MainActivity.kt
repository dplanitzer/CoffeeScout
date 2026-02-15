//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import android.Manifest
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        businessFormatter = BusinessFormatter(resources)
        repository = createBusinessRepository()


        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        viewModel = viewModel,
                        initialStreetAddress = stringResource(R.string.nearby_address),
                        businessFormatter = businessFormatter,
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
