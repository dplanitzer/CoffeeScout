//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout.repository

import com.example.coffeescout.GetBusinessesQuery
import com.example.coffeescout.GeoLocation
import java.time.DayOfWeek

// Holds all the information about a business
data class Business(
    val id: String?,
    val name: String,
    val displayAddress: String,
    val heroImageUrl: String,
    val detailsPageUrl: String,
    val displayPricingLevel: String,
    val rating: Double,
    val reviewCount: Int,
    val distanceInMeters: Double,
    val hours: Hours,
    val geoLocation: GeoLocation?,
    val reviewsUrl: String
    ) {

    // A time value in HH:MM notation (24 hour clock).
    data class HoursMinutes(val hours: Int, val minutes: Int) {

        constructor(t: Int) : this(t / 100,t % 100)
    }

    // An opening hours for a specific day of the week.
    data class OpeningTime(val dayOfWeek: DayOfWeek, val openingAt: HoursMinutes, val closingAt: HoursMinutes) {

        // Constructs an instance from the result of a GraphQL query. Expects that the 'day', 'start'
        // and 'end' properties exist.
        constructor(o: GetBusinessesQuery.Open) : this(
            DayOfWeek.of(o.day!! + 1),
            HoursMinutes(o.start!!.toInt()),
            HoursMinutes(o.end!!.toInt())
        )
    }

    // The opening hours of a business for every day of the week. Also tells you whether the business
    // is open now or whether it is closed.
    data class Hours(val isOpenNow: Boolean, val openingTimes: List<OpeningTime>) {

        constructor(h: GetBusinessesQuery.Hour) : this(
            h.is_open_now ?: false,
            if (h.open != null) {
                h.open.filter { it?.day != null && !it.start.isNullOrEmpty() && !it.end.isNullOrEmpty() }.map { OpeningTime(it!!) }
            } else {
                listOf()
            }
        )
    }


    // Constructs a business object from teh result of a GraphQL query
    constructor(b: GetBusinessesQuery.Business) : this(
        b.id,
        b.name ?: "",
        if (!b.location?.address1.isNullOrEmpty() && !b.location.city.isNullOrEmpty()) {
            b.location.address1 + ", " + b.location.city
        } else if (b.location?.address1 != null) {
            b.location.address1
        } else {
            b.location!!.city!!
        },
        b.photos?.firstOrNull() ?: "",
        b.url ?: "",
        b.price ?: "",
        b.rating ?: 0.0,
        b.review_count ?: 0,
        b.distance ?: 0.0,
        if (b.hours?.firstOrNull() != null) {
            Hours(b.hours.first()!!)
        } else {
            Hours(false, listOf())
        },
        if (b.coordinates?.latitude != null && b.coordinates.longitude != null) {
            GeoLocation(b.coordinates.latitude, b.coordinates.longitude)
        } else {
            null
        },
        b.url ?: "" // XXX use the details page URL for now since querying for review info slows down receiving a response so much...
    )
}
