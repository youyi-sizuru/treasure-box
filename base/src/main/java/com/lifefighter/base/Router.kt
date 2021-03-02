package com.lifefighter.base

import android.os.Parcelable
import kotlin.reflect.KClass

/**
 * @author xzp
 * @created on 2020/10/29.
 */
interface Router {
    fun route(kClass: KClass<*>, parcelable: Parcelable? = null, requestCode: Int? = null)

    fun <T : Parcelable> getBundleNullable(): T?

    fun <T : Parcelable> getBundle(): T {
        return getBundleNullable()
            ?: throw RuntimeException("can't get bundle, please use getBundleNullable")
    }
}