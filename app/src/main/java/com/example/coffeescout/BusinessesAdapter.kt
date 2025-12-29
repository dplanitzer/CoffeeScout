//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import android.content.res.Resources
import android.icu.text.MeasureFormat
import android.icu.util.MeasureUnit
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.coffeescout.repository.Business
import com.example.coffeescout.view.YelpRatingBar
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// The actions that a user might trigger by tapping on the various buttons in a card
enum class BusinessCardAction {
    GoToReviews,        // go to the business reviews page
    GoToDetails,        // go to the business details page
    GoToNavigation,     // go to the navigation app
    GoToYelp,           // go to the Yelp website
}


// The business card action handler. Receives the business card action and the business object on
// which the callback should act.
typealias BusinessCardActionCallback = (BusinessCardAction, Business) -> Unit


// The recycler view adapter that knows how to display and handle a business card
class BusinessesAdapter(
    private val timeFormatter: DateTimeFormatter,
    private val metersFormatter: MeasureFormat,
    private val kilometersFormatter: MeasureFormat,
    private val actionHandler: BusinessCardActionCallback
) : PagedListAdapter<Business, BusinessesAdapter.ViewHolder>(DIFFER) {

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
        actionHandler: BusinessCardActionCallback
    ) : RecyclerView.ViewHolder(view) {

        private val heroImageView: ImageView
        private val nameTextView: TextView
        private val addressTextView: TextView
        private val ratingView: YelpRatingBar
        private val infoTextView: TextView
        private val hoursTextView: TextView

        private var business: Business? = null


        init {

            heroImageView = view.findViewById(R.id.business_image)
            nameTextView = view.findViewById(R.id.business_name)
            addressTextView = view.findViewById(R.id.business_address)
            ratingView = view.findViewById(R.id.business_rating)
            infoTextView = view.findViewById(R.id.business_info)
            hoursTextView = view.findViewById(R.id.business_hours)

            view.findViewById<ImageView>(R.id.business_image).setOnClickListener {
                business?.let { actionHandler(BusinessCardAction.GoToDetails, it) }
            }

            view.findViewById<Button>(R.id.go_to_reviews).setOnClickListener {
                business?.let { actionHandler(BusinessCardAction.GoToReviews, it) }
            }

            view.findViewById<ImageView>(R.id.go_to_yelp).setOnClickListener {
                business?.let { actionHandler(BusinessCardAction.GoToYelp, it) }
            }

            view.findViewById<Button>(R.id.go_to_navigation).setOnClickListener {
                business?.let { actionHandler(BusinessCardAction.GoToNavigation, it) }
            }
        }

        // Updates the view holder with the data from the given business object.
        fun bindTo(b: Business) {

            business = b

            heroImageView.load(b.heroImageUrl) { crossfade(true) }
            nameTextView.text = b.name
            addressTextView.text = b.displayAddress
            ratingView.rating = b.rating
            infoTextView.text = formatBusinessInfo(b)
            hoursTextView.text = formatBusinessHours(b.hours)
        }

        // Returns a formatted string with the review count, pricing level and distance
        private fun formatBusinessInfo(b: Business): String {

            val buf = java.lang.StringBuilder()
            val barSep = resources.getString(R.string.bar_sep)

            // (optional) review count
            if (b.reviewCount > 0) {
                buf.append(resources.getString(R.string.reviews_count_format, b.reviewCount))
            }


            // (optional) pricing level
            if (b.displayPricingLevel.isNotEmpty()) {
                if (buf.isNotEmpty()) {
                    buf.append(barSep)
                }
                buf.append(b.displayPricingLevel)
            }


            // (optional) distance
            if (b.distanceInMeters > 0) {
                if (buf.isNotEmpty()) {
                    buf.append(barSep)
                }

                if (b.distanceInMeters >= 1000.0) {
                    buf.append(kilometersFormatter.formatDistance(b.distanceInMeters / 1000.0, MeasureUnit.KILOMETER))
                } else {
                    buf.append(metersFormatter.formatDistance(b.distanceInMeters, MeasureUnit.METER))
                }
            }

            return buf.toString()
        }

        // Returns a formatted string of the hours of operation
        private fun formatBusinessHours(h: Business.Hours): SpannableString {

            val openClosedString = resources.getString(if (h.isOpenNow) R.string.business_open else R.string.business_closed)

            val buf = java.lang.StringBuilder()
            val openingTimesString = formatOpeningTimes(h.openingTimes)

            buf.append(openClosedString)
            if (openingTimesString.isNotEmpty()) {
                buf.append(resources.getString(R.string.bullet_sep))
                buf.append(openingTimesString)
            }

            val ss = SpannableString(buf.toString())
            ss.setSpan(ForegroundColorSpan(resources.getColor(if (h.isOpenNow) R.color.business_open else R.color.business_closed, null)), 0, openClosedString.length, 0)
            return ss
        }

        // Returns a formatted string with the opening hours of the business. We only take the hours
        // for today into account.
        private fun formatOpeningTimes(openingTimes: List<Business.OpeningTime>): String {

            val todaysDayOfWeek = LocalDate.now().dayOfWeek
            val buf = java.lang.StringBuilder()

            for (ot in openingTimes) {
                if (ot.dayOfWeek == todaysDayOfWeek) {
                    buf.append(formatHoursAndMinutes(ot.openingAt))
                    buf.append(resources.getString(R.string.dash_sep))
                    buf.append(formatHoursAndMinutes(ot.closingAt))
                    break
                }
            }

            return buf.toString()
        }

        // Returns a formatted string of the given hours and minutes. They are assumed to be in
        // 24 hour format.
        private fun formatHoursAndMinutes(t: Business.HoursMinutes): String {

            return timeFormatter.format(LocalTime.of(t.hours, t.minutes))
        }

        private val resources: Resources
            get() = heroImageView.resources
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