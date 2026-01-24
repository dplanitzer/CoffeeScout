//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import android.icu.text.MeasureFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeescout.repository.Business
import java.time.format.DateTimeFormatter


// The recycler view adapter that knows how to display and handle a business card
class BusinessesAdapter(
    private val timeFormatter: DateTimeFormatter,
    private val metersFormatter: MeasureFormat,
    private val kilometersFormatter: MeasureFormat,
    private val actionHandler: BusinessCardActionCallback
) : PagingDataAdapter<Business, BusinessesAdapter.ViewHolder>(DIFFER) {

    companion object {
        private val DIFFER = object :
            DiffUtil.ItemCallback<Business>() {
            override fun areItemsTheSame(lhs: Business, rhs: Business) = lhs.id == rhs.id

            override fun areContentsTheSame(lhs: Business, rhs: Business) = lhs == rhs
        }
    }

    class ViewHolder(
        view: View,
        private val timeFormatter: DateTimeFormatter,
        private val metersFormatter: MeasureFormat,
        private val kilometersFormatter: MeasureFormat,
        private val actionHandler: BusinessCardActionCallback
    ) : RecyclerView.ViewHolder(view) {

        private val composeView: ComposeView = view.findViewById(R.id.compose_view)
        private var business: Business? = null

        // Updates the view holder with the data from the given business object.
        fun bindTo(b: Business) {

            business = b

            composeView.setContent {
                // You're in Compose world!
                MaterialTheme {
                    BusinessCardHolder(b, timeFormatter, metersFormatter, kilometersFormatter, actionHandler)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.business_card, parent, false)
        return ViewHolder(view, timeFormatter, metersFormatter, kilometersFormatter, actionHandler)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val business = getItem(position) ?: return
        holder.bindTo(business)
    }
}