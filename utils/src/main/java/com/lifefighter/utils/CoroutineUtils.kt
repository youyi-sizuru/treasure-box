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

val ignoreErrorHandler = CoroutineExceptionHandler { _, _ -> }

fun CoroutineScope.launch(
    prepare: (suspend () -> Unit)? = null,
    failure: (suspend (t: Throwable) -> Unit)? = null,
    final: (suspend () -> Unit)? = null,
    task: suspend CoroutineScope.() -> Unit
): Job {
    return this.launch(ignoreErrorHandler) {
        try {
            prepare?.invoke()
            task()
        } catch (throwable: Throwable) {
            if (throwable !is CancellationException) {
                logError("launch run error", throwable)
                try {
                    failure?.invoke(throwable)
                } catch (t: Throwable) {
                    logError("launch failure error", throwable)
                }
            }
        } finally {
            try {
                final?.invoke()
            } catch (throwable: Throwable) {
                logError("launch final error", throwable)
            }
        }
    }
}