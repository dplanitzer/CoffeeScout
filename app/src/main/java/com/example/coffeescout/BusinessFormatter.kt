package com.example.coffeescout

import android.content.res.Resources
import android.icu.text.MeasureFormat
import android.icu.text.NumberFormat
import android.icu.util.MeasureUnit
import com.example.coffeescout.repository.Business
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class BusinessFormatter(resources: Resources) {

    // Create formatters for showing hours & minutes, meters and kilometers
    private val curLocale = resources.configuration.locales[0]
    private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    private val metersFormatter = MeasureFormat.getInstance(curLocale, MeasureFormat.FormatWidth.NARROW, NumberFormat.getIntegerInstance(curLocale))
    private val kilometersNumberFormatter = NumberFormat.getNumberInstance(curLocale)
    private val kilometersFormatter = MeasureFormat.getInstance(curLocale, MeasureFormat.FormatWidth.NARROW, kilometersNumberFormatter)

    init {
        kilometersNumberFormatter.minimumFractionDigits = 0
        kilometersNumberFormatter.maximumFractionDigits = 1
    }

    // Returns a formatted string with the review count, pricing level and distance
    fun formatInfo(b: Business, separator: String): String {

        val buf = java.lang.StringBuilder()

        // (optional) review count
        if (b.reviewCount > 0) {
            buf.append('(')
            buf.append(b.reviewCount)
            buf.append(')')
        }


        // (optional) pricing level
        if (b.displayPricingLevel.isNotEmpty()) {
            if (buf.isNotEmpty()) {
                buf.append(separator)
            }
            buf.append(b.displayPricingLevel)
        }


        // (optional) distance
        if (b.distanceInMeters > 0) {
            if (buf.isNotEmpty()) {
                buf.append(separator)
            }

            if (b.distanceInMeters >= 1000.0) {
                buf.append(kilometersFormatter.formatDistance(b.distanceInMeters / 1000.0, MeasureUnit.KILOMETER))
            } else {
                buf.append(metersFormatter.formatDistance(b.distanceInMeters, MeasureUnit.METER))
            }
        }

        return buf.toString()
    }

    // Returns a formatted string with the opening hours of the business. We only take the hours
    // for today into account.
    fun formatOpeningTimes(openingTimes: List<Business.OpeningTime>, separator: String): String {

        val todaysDayOfWeek = LocalDate.now().dayOfWeek
        val buf = java.lang.StringBuilder()

        for (ot in openingTimes) {
            if (ot.dayOfWeek == todaysDayOfWeek) {
                buf.append(formatHoursAndMinutes(ot.openingAt))
                buf.append(separator)
                buf.append(formatHoursAndMinutes(ot.closingAt))
                break
            }
        }

        return buf.toString()
    }

    // Returns a formatted string of the given hours and minutes. They are assumed to be in
    // 24 hour format.
    fun formatHoursAndMinutes(t: Business.HoursMinutes): String {

        return timeFormatter.format(LocalTime.of(t.hours, t.minutes))
    }
}