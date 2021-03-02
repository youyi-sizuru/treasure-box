package com.lifefighter.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.lifefighter.utils.EventBusManager
import com.lifefighter.utils.orFalse
import com.lifefighter.utils.tryOrNull
import kotlinx.coroutines.*
import org.greenrobot.eventbus.Subscribe
import org.koin.androidx.scope.lifecycleScope

/**
 * @author xzp
 * @created on 2020/10/19.
 */
interface LifecycleBinder<T : ViewDataBinding> : LifecycleOwner, CoroutineScope, Router {
    val viewBinding: T
    val rootActivity: BaseActivity<*>
    val rootContext: Context
        get() = rootActivity
    val activityScope
        get() = rootActivity.lifecycleScope

    val currentFragmentManager: FragmentManager

    fun onLifecycleInit(savedInstanceState: Bundle?) {}

    fun onLifecycleStart() {}

    fun onLifecycleStop() {}

    fun onLifecycleDestroy() {}

    fun getLayoutId(): Int

    fun finish()

    fun createViewBinding(parent: ViewGroup? = null): T {
        return DataBindingUtil.inflate<T>(
            LayoutInflater.from(rootContext),
            getLayoutId(),
            parent,
            false
        )
            .also {
                it.lifecycleOwner = this@LifecycleBinder
            }
    }

    fun registerEventBus() {
        val canRegister = tryOrNull {
            this.javaClass.declaredMethods.any {
                it.getAnnotation(Subscribe::class.java) != null
            }
        }.orFalse()
        if (canRegister) {
            EventBusManager.register(this)
        }
    }

    fun unregisterEventBus() {
        val canUnregister = tryOrNull {
            this.javaClass.declaredMethods.any {
                it.getAnnotation(Subscribe::class.java) != null
            }
        }.orFalse()
        if (canUnregister) {
            EventBusManager.unregister(this)
        }
    }

    override val coroutineContext get() = lifecycle.coroutineScope.coroutineContext

    fun launch(
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
                    Log.e("LifecycleBinder", "launch run error", throwable)
                    try {
                        failure?.invoke(throwable)
                    } catch (t: Throwable) {
                        Log.e("LifecycleBinder", "launch failure error", throwable)
                    }
                }
            } finally {
                try {
                    final?.invoke()
                } catch (throwable: Throwable) {
                    Log.e("LifecycleBinder", "launch final error", throwable)
                }
            }
        }
    }
}

private val ignoreErrorHandler = CoroutineExceptionHandler { _, _ -> }

fun LifecycleBinder<*>.color(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(rootContext, colorId)
}

fun LifecycleBinder<*>.string(@StringRes stringId: Int): String {
    return rootContext.getString(stringId)
}

fun LifecycleBinder<*>.drawable(@DrawableRes drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(rootContext, drawableId)
}

fun LifecycleBinder<*>.hideSoftKeyboard() {
    (rootActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(
        rootActivity.window.decorView.windowToken,
        0
    )
}

fun LifecycleBinder<*>.dp2px(dp: Int): Int = (rootContext.resources.displayMetrics.density * dp).toInt()