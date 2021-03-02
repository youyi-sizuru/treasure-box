package com.lifefighter.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

/**
 * @author xzp
 * @created on 2020/10/23.
 */
object AppConfigsUtils {
    private var sharedPreferences: SharedPreferences? = null


    fun init(application: Application, name: String = "app_configs") {
        sharedPreferences = application.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun putBoolean(name: String, value: Boolean) {
        getSp().edit().putBoolean(name, value).apply()
    }

    fun getBoolean(name: String, defaultValue: Boolean): Boolean {
        return getSp().getBoolean(name, defaultValue)
    }

    fun putString(name: String, value: String?) {
        getSp().edit().putString(name, value).apply()
    }

    fun getString(name: String, defaultValue: String?): String? {
        return getSp().getString(name, defaultValue)
    }

    fun remove(name: String) {
        getSp().edit().remove(name).apply()
    }

    private fun getSp(): SharedPreferences {
        return sharedPreferences ?: throw RuntimeException("AppConfigsUtils must call init first!")
    }
}

abstract class AppConfigItem<T>(val name: String, val defaultValue: T) {
    abstract fun put(value: T)
    abstract fun get(): T
    fun clear() {
        AppConfigsUtils.remove(name)
    }
}

class BooleanConfigItem(name: String, defaultValue: Boolean = false) :
    AppConfigItem<Boolean>(name, defaultValue) {
    override fun put(value: Boolean) {
        AppConfigsUtils.putBoolean(name, value)
    }

    override fun get(): Boolean {
        return AppConfigsUtils.getBoolean(name, defaultValue)
    }
}

class StringConfigItem(name: String, defaultValue: String? = null) :
    AppConfigItem<String?>(name, defaultValue) {
    override fun put(value: String?) {
        AppConfigsUtils.putString(name, value)
    }

    override fun get(): String? {
        return AppConfigsUtils.getString(name, defaultValue)
    }

    fun putJson(value: Any?) {
        put(value?.toJson())
    }

    inline fun <reified T : Any> getJson(): T? {
        return get()?.parseJsonOrNull()
    }
}
