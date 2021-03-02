package com.lifefighter.utils

import timber.log.Timber


/**
 * @author xzp
 * @created on 2020/11/13.
 */
object LogUtils {
    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

fun logVerbose(throwable: Throwable, message: String? = null) {
    if (message == null) {
        Timber.v(throwable)
    } else {
        Timber.v(throwable, message)
    }
}

fun logDebug(throwable: Throwable, message: String? = null) {
    if (message == null) {
        Timber.d(throwable)
    } else {
        Timber.d(throwable, message)
    }
}

fun logInfo(throwable: Throwable, message: String? = null) {
    if (message == null) {
        Timber.i(throwable)
    } else {
        Timber.i(throwable, message)
    }
}

fun logError(throwable: Throwable, message: String? = null) {
    if (message == null) {
        Timber.e(throwable)
    } else {
        Timber.e(throwable, message)
    }
}

fun logWarn(throwable: Throwable, message: String? = null) {
    if (message == null) {
        Timber.w(throwable)
    } else {
        Timber.w(throwable, message)
    }
}

fun logVerbose(message: String) {
    Timber.v(message)
}

fun logDebug(message: String) {
    Timber.d(message)
}

fun logInfo(message: String) {
    Timber.i(message)
}

fun logError(message: String) {
    Timber.e(message)
}

fun logWarn(message: String) {
    Timber.w(message)
}