package com.example.screen_resources

import android.content.Context
import android.util.TypedValue
import java.util.concurrent.TimeUnit

fun Int.toDP(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    )
}

fun Int.toMinutes(): String = String.format(
    "%02d : %02d",
    TimeUnit.MILLISECONDS.toMinutes(this.toLong()),
    TimeUnit.MILLISECONDS.toSeconds(this.toLong()) -
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.toLong()))
)
