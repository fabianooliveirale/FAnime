package com.example.screen_resources.extensions

import android.app.Activity
import android.graphics.Point
import android.os.Build
import android.view.Display
import android.view.WindowInsets


fun Activity.windowHeightDeprecated(): Int {
    val display: Display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)

    return size.y
}

fun Activity.windowWidthDeprecated(): Int {
    val display: Display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)

    return size.x
}


fun Activity.windowWidth(): Int {
    val metrics = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        windowManager.currentWindowMetrics
    } else {
       return this.windowWidthDeprecated()
    }

    val windowInsets = metrics.windowInsets
    val insets: android.graphics.Insets = windowInsets.getInsetsIgnoringVisibility(
        WindowInsets.Type.navigationBars()
                or WindowInsets.Type.displayCutout()
    )

    return insets.right + insets.left
}

fun Activity.windowHeight(): Int {
    val metrics = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        windowManager.currentWindowMetrics
    } else {
        return windowHeightDeprecated()
    }

    val windowInsets = metrics.windowInsets
    val insets: android.graphics.Insets = windowInsets.getInsetsIgnoringVisibility(
        WindowInsets.Type.navigationBars()
                or WindowInsets.Type.displayCutout()
    )


    return insets.top + insets.bottom
}
