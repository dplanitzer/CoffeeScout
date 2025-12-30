//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import android.icu.text.MeasureFormat
import android.icu.text.NumberFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeescout.repository.Business
import com.example.coffeescout.repository.createBusinessRepository
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : AppCompatActivity() {

    companion object {
        const val USER_ADDRESS = "85 Pike Street, Seattle, WA"
        const val BUSINESS_CATEGORY = "coffee"
        const val BUSINESS_SORTING = "distance"
        const val INITIAL_LOAD_SIZE = 10
        const val LOAD_SIZE = 10

        const val YELP_URL = "https://www.yelp.com"
    }


    private lateinit var viewModel: MainViewModel
    private lateinit var businessesAdapter: BusinessesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create formatters for showing hours & minutes, meters and kilometers
        val curLocale = resources.configuration.locales[0]
        val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        val metersFormatter = MeasureFormat.getInstance(curLocale, MeasureFormat.FormatWidth.NARROW, NumberFormat.getIntegerInstance(curLocale))
        val kilometersNumberFormatter = NumberFormat.getNumberInstance(curLocale)
        kilometersNumberFormatter.minimumFractionDigits = 0
        kilometersNumberFormatter.maximumFractionDigits = 1
        val kilometersFormatter = MeasureFormat.getInstance(curLocale, MeasureFormat.FormatWidth.NARROW, kilometersNumberFormatter)


        // Create the businesses repository and view model
        val repository = createBusinessRepository()
        viewModel = ViewModelProvider(this, MainViewModelFactory(
            repository,
            USER_ADDRESS,
            BUSINESS_CATEGORY,
            BUSINESS_SORTING,
            INITIAL_LOAD_SIZE,
            LOAD_SIZE)
        )[MainViewModel::class.java]


        // Create the businesses adapter for the recycle view
        businessesAdapter = BusinessesAdapter(timeFormatter, metersFormatter, kilometersFormatter, this::onBusinessCardAction)
        findViewById<RecyclerView>(R.id.recycler_view).adapter = businessesAdapter


        // Observe changes in the view model and act on them
        viewModel.liveData.observe(this) {
            businessesAdapter.submitList(it)
        }
        viewModel.liveError.observe(this) {
            if (it == null) {
                setErrorViewVisible(false)
            } else {
                showErrorMessage(resources.getString(R.string.network_error_msg))
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

    // Show the given error message and hide the recycyler view while we are displaying the error.
    private fun showErrorMessage(msg: String) {

        findViewById<TextView>(R.id.error_msg).text = msg
        findViewById<Button>(R.id.error_retry_button).setOnClickListener {

            viewModel.invalidate()
        }

        setErrorViewVisible(true)
    }

    // Show or hide the error view. The recycler view visibility is always the opposite of the
    // error view visibility.
    private fun setErrorViewVisible(doShow: Boolean) {

        findViewById<LinearLayout>(R.id.error_view).isVisible = doShow
        findViewById<RecyclerView>(R.id.recycler_view).isVisible = !doShow
    }
}