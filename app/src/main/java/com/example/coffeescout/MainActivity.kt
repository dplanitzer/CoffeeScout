//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.coffeescout.repository.Business
import com.example.coffeescout.repository.BusinessesRepository
import com.example.coffeescout.repository.createBusinessRepository

class MainActivity : AppCompatActivity() {

    companion object {
        const val INITIAL_STREET_ADDRESS = "85 Pike Street, Seattle, WA"
        const val BUSINESS_CATEGORY = "coffee"
        const val BUSINESS_SORTING = "distance"
        const val INITIAL_LOAD_SIZE = 10
        const val LOAD_SIZE = 10

        const val YELP_URL = "https://www.yelp.com"
    }


    private lateinit var repository: BusinessesRepository

    private lateinit var businessFormatter: BusinessFormatter

    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(
        repository,
        INITIAL_STREET_ADDRESS,
        BUSINESS_CATEGORY,
        BUSINESS_SORTING,
        INITIAL_LOAD_SIZE,
        LOAD_SIZE) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create formatters for showing hours & minutes, meters and kilometers
        businessFormatter = BusinessFormatter(resources)
        repository = createBusinessRepository()


        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        viewModel = viewModel,
                        initialStreetAddress = INITIAL_STREET_ADDRESS,
                        businessFormatter = businessFormatter,
                        onAction = this::onBusinessCardAction
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
}