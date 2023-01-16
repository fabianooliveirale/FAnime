package com.example.screen_resources.extensions

import com.example.screen_resources.isInt

fun List<String>.getLastItemNumber(count: Int): String? {
    return try {
        val isInt = this[count].isInt() ?: false
        if (isInt) {
            return this[count]
        } else {
            this.getLastItemNumber(count - 1)
        }
    } catch (e: Exception) {
        return ""
    }
}