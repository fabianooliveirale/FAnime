package com.example.screen_resources

import android.content.Context
import android.util.TypedValue

fun Int.toDP(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    )
}