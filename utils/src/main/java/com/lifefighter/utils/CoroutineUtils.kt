package com.lifefighter.utils

import kotlinx.coroutines.*

/**
 * @author xzp
 * @created on 2020/10/23.
 */
suspend fun <T> bg(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.IO, block)
}

@Suppress("DeferredIsResult")
inline fun <T> CoroutineScope.bgImmediately(crossinline block: suspend () -> T): Deferred<T> {
    return async(Dispatchers.IO) { block() }
}

suspend fun <T> ui(block: suspend CoroutineScope.() -> T): T {
    return withContext(Dispatchers.Main.immediate, block)
}