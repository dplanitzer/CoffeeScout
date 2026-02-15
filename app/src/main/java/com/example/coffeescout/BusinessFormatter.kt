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