package com.lifefighter.utils

import android.app.Application
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * @author xzp
 * @created on 2020/11/20.
 */
object ToastUtils {
    private var mApplication: Application? = null
    fun init(application: Application) {
        mApplication = application
    }

    fun toast(message: CharSequence) {
        mApplication?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        } ?: throw RuntimeException("ToastUtils must call init first")
    }

    fun toast(@StringRes id: Int) {
        mApplication?.let {
            Toast.makeText(it, id, Toast.LENGTH_SHORT).show()
        } ?: throw RuntimeException("ToastUtils must call init first")
    }
}

fun toast(message: CharSequence) {
    ToastUtils.toast(message)
}

fun toast(@StringRes id: Int) {
    ToastUtils.toast(id)
}