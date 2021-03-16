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


fun logVerbose(message: String? = null, throwable: Throwable? = null) {
    if (throwable != null) {
        if (message != null) {
            Timber.v(throwable, message)
        } else {
            Timber.v(throwable)
        }
    } else if (message != null) {
        Timber.v(message)
    }
}

fun logDebug(message: String? = null, throwable: Throwable? = null) {
    if (throwable != null) {
        if (message != null) {
            Timber.d(throwable, message)
        } else {
            Timber.d(throwable)
        }
    } else if (message != null) {
        Timber.d(message)
    }
}

fun logInfo(message: String? = null, throwable: Throwable? = null) {
    if (throwable != null) {
        if (message != null) {
            Timber.i(throwable, message)
        } else {
            Timber.i(throwable)
        }
    } else if (message != null) {
        Timber.i(message)
    }
}

fun logError(message: String? = null, throwable: Throwable? = null) {
    if (throwable != null) {
        if (message != null) {
            Timber.e(throwable, message)
        } else {
            Timber.e(throwable)
        }
    } else if (message != null) {
        Timber.e(message)
    }
}

fun logWarn(message: String? = null, throwable: Throwable? = null) {
    if (throwable != null) {
        if (message != null) {
            Timber.w(throwable, message)
        } else {
            Timber.w(throwable)
        }
    } else if (message != null) {
        Timber.w(message)
    }
}