package com.lifefighter.base

import android.content.Context


val Context.windowWidth: Int
    get() {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.widthPixels
    }

val Context.windowHeight: Int
    get() {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.heightPixels
    }
