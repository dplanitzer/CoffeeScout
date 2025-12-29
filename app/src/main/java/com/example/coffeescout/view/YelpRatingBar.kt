//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.coffeescout.R
import kotlin.math.max
import kotlin.math.min

// A Yelp style rating bar. See the display requirements here:
// https://www.yelp.com/developers/display_requirements
class YelpRatingBar@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val stars = listOf(
        ResourcesCompat.getDrawable(resources, R.drawable.stars_small_0, null)!!,
        ResourcesCompat.getDrawable(resources, R.drawable.stars_small_1, null)!!,
        ResourcesCompat.getDrawable(resources, R.drawable.stars_small_2, null)!!,
        ResourcesCompat.getDrawable(resources, R.drawable.stars_small_3, null)!!,
        ResourcesCompat.getDrawable(resources, R.drawable.stars_small_4, null)!!,
        ResourcesCompat.getDrawable(resources, R.drawable.stars_small_5, null)!!,
    )

    private val starsHalf = listOf(
        ResourcesCompat.getDrawable(resources, R.drawable.stars_small_1_half, null)!!,
        ResourcesCompat.getDrawable(resources, R.drawable.stars_small_2_half, null)!!,
        ResourcesCompat.getDrawable(resources, R.drawable.stars_small_3_half, null)!!,
        ResourcesCompat.getDrawable(resources, R.drawable.stars_small_4_half, null)!!,
    )

    private val gravity = Gravity.CENTER_VERTICAL

    // Cached bounding boxes for layouting
    private val selfBounds = Rect()
    private val drawableBounds = Rect()


    // The current rating
    var rating: Double = 0.0
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    // The intrinsic width & height of the stars. We're currently assuming that they all are of the
    // same size.
    private val intrinsicStarWidth: Int
        get() = stars[0].intrinsicWidth

    private val intrinsicStarHeight: Int
        get() = stars[0].intrinsicHeight

    // Returns the stars drawable corresponding to the current rating value
    private val currentRatingDrawable: Drawable
        get() {
            val r = max(0.0, min(5.0, rating))
            val ir = r.toInt()
            val isHalf = (r - ir.toDouble()) >= 0.5

            return if (!isHalf) {
                stars[ir]
            } else {
                starsHalf[max(1, min(4, ir)) - 1]
            }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val contWidth = paddingLeft + intrinsicStarWidth + paddingRight
        val contHeight = paddingTop + intrinsicStarHeight + paddingBottom

        setMeasuredDimension(resolveSize(contWidth, widthMeasureSpec),
            resolveSize(contHeight, heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas) {

        val d = currentRatingDrawable
        selfBounds.set(0, 0, width, height)
        Gravity.apply(gravity, intrinsicStarWidth, intrinsicStarHeight, selfBounds, drawableBounds)
        d.bounds = drawableBounds
        d.draw(canvas)
    }
}