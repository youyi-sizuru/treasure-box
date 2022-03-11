package com.lifefighter.widget.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.annotation.TargetApi
import android.graphics.Path
import android.graphics.Point
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.coroutineScope
import com.lifefighter.utils.getBoundsInScreen
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.math.hypot
import kotlin.random.Random

/**
 * @author xzp
 * @created on 2022/2/10.
 */
abstract class ExAccessibilityService : AccessibilityService(), LifecycleOwner, CoroutineScope {
    private val mDispatcher = ServiceLifecycleDispatcher(this)

    override fun getLifecycle(): Lifecycle {
        return mDispatcher.lifecycle
    }

    override val coroutineContext: CoroutineContext
        get() = lifecycle.coroutineScope.coroutineContext

    override fun onCreate() {
        mDispatcher.onServicePreSuperOnCreate()
        super.onCreate()
    }

    override fun onServiceConnected() {
        mDispatcher.onServicePreSuperOnStart()
        onStart()
    }

    override fun onInterrupt() {
        mDispatcher.onServicePreSuperOnDestroy()
        onStop()
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
        clickPoint(Point(pointX, pointY))
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun clickPoint(point: Point) {
        val path = Path()
        path.moveTo(point.x.toFloat(), point.y.toFloat())
        path.lineTo(point.x.toFloat(), point.y.toFloat())
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

    @TargetApi(Build.VERSION_CODES.N)
    fun scrollTo(from: Point, to: Point) {
        val path = Path()
        path.moveTo(from.x.toFloat(), from.y.toFloat())
        path.lineTo(to.x.toFloat(), to.y.toFloat())
        val distance = hypot((to.x - from.x).toDouble(), (to.y - from.y).toDouble()).toInt()
        val desc =
            GestureDescription.Builder()
                .addStroke(
                    GestureDescription.StrokeDescription(
                        path,
                        Random.nextLong(1, 20),
                        distance + Random.nextLong(1, 50)
                    )
                )
                .build()
        dispatchGesture(desc, null, null)
    }

    fun back() {
        performGlobalAction(GLOBAL_ACTION_BACK)
    }
}