package com.lifefighter.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Point
import android.os.Build

/**
 * @author xzp
 * @created on 2020/11/20.
 */
fun Context.isMainProcess(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        return Application.getProcessName() == packageName
    }
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

fun Context.getScreenRealSize(): Point {
    val screenPoint = Point(1, 1)
    val display = resources.displayMetrics
    screenPoint.x = display.widthPixels
    screenPoint.y = display.heightPixels
    return screenPoint
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
