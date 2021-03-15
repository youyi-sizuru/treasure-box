package com.lifefighter.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.ContextWrapper

/**
 * @author xzp
 * @created on 2020/11/20.
 */
fun Context.isMainProcess(): Boolean {
    val am = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return false
    val infos = am.runningAppProcesses
    if (infos.isNullOrEmpty()) {
        return false
    }
    val pid = android.os.Process.myPid()
    for (info in infos) {
        if (info.pid == pid && packageName == info.processName) {
            return true
        }
    }
    return false
}

fun Context.getRootActivity(): Activity? {
    return when (this) {
        is Activity -> {
            this
        }
        is ContextWrapper -> {
            this.baseContext.getRootActivity()
        }
        else -> {
            null
        }
    }
}
