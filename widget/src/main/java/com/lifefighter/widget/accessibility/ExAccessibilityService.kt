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
import kotlin.random.Random

/**
 * @author xzp
 * @created on 2022/2/10.
 */
abstract class ExAccessibilityService : AccessibilityService() {
    private var mSafeTime: Long = 0L
    override fun onServiceConnected() {
        onStart()
    }

    override fun onInterrupt() {
        onStop()
    }

    abstract fun onStart()
    abstract fun onStop()
    abstract fun onReceiveEvent(event: AccessibilityEvent?)

    @CallSuper
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        //2秒的保护机制
        if (System.currentTimeMillis() - mSafeTime > 2000) {
            onReceiveEvent(event)
        }
    }

    fun keepSafe() {
        mSafeTime = System.currentTimeMillis()
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