package com.lifefighter.utils

import android.util.Log

/**
 * @author xzp
 * @created on 2020/10/30.
 */

inline fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block.invoke()
    } catch (throwable: Throwable) {
        Log.e("tryOrNull", "run block error", throwable)
        null
    }
}