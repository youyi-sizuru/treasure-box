package com.lifefighter.base

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.CoroutineScope

/**
 * @author xzp
 * @created on 2022/2/14.
 */
abstract class BaseService : LifecycleService(), CoroutineScope {
    override val coroutineContext get() = lifecycle.coroutineScope.coroutineContext
}