package com.lifefighter.utils

import com.alibaba.fastjson.JSON
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type


/**
 * @author xzp
 * @created on 2018/10/24.
 */

object JsonUtils {
    ///just for parse
    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(StringToIntAdapter())
        .build()

    fun newParameterizedType(
        rawType: Type,
        vararg typeArguments: Type
    ): Type {
        return Types.newParameterizedType(rawType, *typeArguments)
    }
}

class StringToIntAdapter {
    @FromJson
    fun fromJson(json: String?): Int? {
        return json?.toIntOrNull()
    }
}


fun <T : Any> T.toJson(): String {
    return JSON.toJSONString(this)
}

fun <T : Any> T?.toJsonOrEmpty() = this?.toJson() ?: "{}"

/**
 * 把Any用list抱起来解析成json字符串
 */
fun Any.toListJson() = listOf(this).toJson()

fun <T : Any> List<T>.toJsonOrNull() = if (this.isNotEmpty()) toJson() else null

fun <T : Any> List<T>?.toJsonOrEmpty() = this?.toJson() ?: "[]"

inline fun <reified T : Any> String?.parseListJson(): List<T> = parseParameterizedJsonOrNull(
    List::class.java,
    T::class.java
) ?: emptyList()


inline fun <reified T : Any> String?.parseListJsonOrNull() = try {
    this?.parseListJson<T>()
} catch (e: Exception) {
    null
}

fun <T : Any> String?.parseParameterizedJsonOrNull(
    rawType: Type,
    vararg typeArguments: Type
): T? {
    return this?.let {
        val type: Type = JsonUtils.newParameterizedType(rawType, *typeArguments)
        val adapter = JsonUtils.moshi.adapter<T>(type).lenient()
        adapter.fromJson(this)
    }
}

fun <T : Any> String.parseParameterizedJson(
    rawType: Type,
    vararg typeArguments: Type
): T {
    return parseParameterizedJsonOrNull(rawType, *typeArguments)
        ?: throw Exception("$this can't be parse to json")
}

inline fun <reified T : Any> String?.parseMapJson(): Map<String, T> =
    parseParameterizedJsonOrNull(
        Map::class.java,
        String::class.javaObjectType,
        T::class.javaObjectType
    ) ?: emptyMap()

inline fun <reified T : Any> String.parseJsonOrNull(): T? {
    val adapter = JsonUtils.moshi.adapter(T::class.java).lenient()
    return adapter.fromJson(this)
}

inline fun <reified T : Any> String.parseJson(): T {
    return this.parseJsonOrNull()
        ?: throw Exception("$this can't be parse to json")
}



