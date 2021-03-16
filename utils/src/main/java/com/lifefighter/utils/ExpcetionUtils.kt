package com.lifefighter.utils

/**
 * @author xzp
 * @created on 2020/10/30.
 */

inline fun <T> tryOrNull(block: () -> T): T? {
    return try {
        block.invoke()
    } catch (throwable: Throwable) {
        logError("tryOrNull run block error", throwable)
        null
    }
}

inline fun tryOrNothing(block: () -> Unit) {
    try {
        block.invoke()
    } catch (throwable: Throwable) {
        logError("tryOrNothing run block error", throwable)
    }
}