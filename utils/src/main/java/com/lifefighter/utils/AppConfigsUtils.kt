package com.lifefighter.utils

import android.app.Application
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel

/**
 * @author xzp
 * @created on 2020/10/23.
 */
object AppConfigsUtils {
    fun init(application: Application, name: String = "app_configs") {
        MMKV.initialize(
            application,
            if (BuildConfig.DEBUG) MMKVLogLevel.LevelInfo else MMKVLogLevel.LevelError
        )
    }

    fun putBoolean(name: String, value: Boolean) {
        getSp().encode(name, value)
    }

    fun getBoolean(name: String, defaultValue: Boolean): Boolean {
        return getSp().decodeBool(name, defaultValue)
    }

    fun putString(name: String, value: String?) {
        getSp().encode(name, value)
    }

    fun getString(name: String, defaultValue: String?): String? {
        return getSp().decodeString(name, defaultValue)
    }

    fun remove(name: String) {
        getSp().removeValueForKey(name)
    }

    private fun getSp(): MMKV {
        return MMKV.defaultMMKV()
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
