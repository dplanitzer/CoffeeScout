//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeescout.repository.Business


// The recycler view adapter that knows how to display and handle a business card
class BusinessesAdapter(
    private val businessFormatter: BusinessFormatter,
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
        private val businessFormatter: BusinessFormatter,
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
                    BusinessCardHolder(b, businessFormatter, actionHandler)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.business_card, parent, false)
        return ViewHolder(view, businessFormatter, actionHandler)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val business = getItem(position) ?: return
        holder.bindTo(business)
    }
}