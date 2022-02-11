package com.lifefighter.widget.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.annotation.TargetApi
import android.graphics.Path
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.CallSuper
import com.lifefighter.utils.getBoundsInScreen
import com.lifefighter.utils.tryOrNothing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

/**
 * @author xzp
 * @created on 2022/2/10.
 */
abstract class ExAccessibilityService : AccessibilityService(), CoroutineScope {
    private val mCoroutineContextRef = AtomicReference<CoroutineContext?>()
    private var mInterrupt: Boolean = false
    override val coroutineContext: CoroutineContext
        get() {
            while (true) {
                val existing = mCoroutineContextRef.get()
                if (existing != null) {
                    return existing
                }
                val newContext = SupervisorJob() + Dispatchers.Main.immediate
                if (mInterrupt) {
                    newContext.cancel()
                }
                if (mCoroutineContextRef.compareAndSet(null, newContext)) {
                    return newContext
                }
            }
        }

    override fun onServiceConnected() {
        mInterrupt = false
        onStart()
    }

    override fun onInterrupt() {
        onStop()
        mInterrupt = true
        tryOrNothing {
            mCoroutineContextRef.get()?.cancel()
            mCoroutineContextRef.lazySet(null)
        }
    }

    open fun onStart() {}
    open fun onStop() {}
    abstract fun onReceiveEvent(event: AccessibilityEvent?)

    @CallSuper
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        onReceiveEvent(event)
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun clickNode(nodeInfo: AccessibilityNodeInfo) {
        val bounds = nodeInfo.getBoundsInScreen()
        val pointX = Random.nextInt(
            bounds.left + bounds.width() / 4,
            bounds.right - bounds.width() / 4
        )
        val pointY = Random.nextInt(
            bounds.top + bounds.height() / 4,
            bounds.bottom - bounds.height() / 4
        )
        val path = Path()
        path.moveTo(pointX.toFloat(), pointY.toFloat())
        path.lineTo(pointX.toFloat(), pointY.toFloat())
        val desc =
            GestureDescription.Builder()
                .addStroke(
                    GestureDescription.StrokeDescription(
                        path,
                        Random.nextLong(1, 20),
                        Random.nextLong(1, 50)
                    )
                )
                .build()
        dispatchGesture(desc, null, null)
    }

    fun back() {
        performGlobalAction(GLOBAL_ACTION_BACK)
    }
}