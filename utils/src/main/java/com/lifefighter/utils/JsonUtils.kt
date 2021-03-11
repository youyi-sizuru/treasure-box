package com.lifefighter.utils

import com.alibaba.fastjson.JSON
import com.google.gson.GsonBuilder
import com.google.gson.JsonIOException
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import java.io.Reader
import java.lang.reflect.Modifier
import java.lang.reflect.Type


/**
 * @author xzp
 * @created on 2018/10/24.
 */

object JsonUtils {
    val gson = GsonBuilder().excludeFieldsWithModifiers(
        Modifier.STATIC,
        Modifier.TRANSIENT
    ).create()

    fun newParameterizedType(
        rawType: Type,
        vararg typeArguments: Type
    ): Type {
        return TypeToken.getParameterized(rawType, *typeArguments).type
    }

    fun <T> read(reader: Reader, type: Type): T? {
        return gson.newJsonReader(reader).use {
            val result: T? = gson.getAdapter(TypeToken.get(type)).read(it) as? T
            if (it.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            result
        }
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
        JsonUtils.gson.fromJson<T>(this, type)
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
    return tryOrNull {
        JsonUtils.gson.fromJson(this, T::class.java)
    }
}

inline fun <reified T : Any> String.parseJson(): T {
    return this.parseJsonOrNull()
        ?: throw Exception("$this can't be parse to json")
}



