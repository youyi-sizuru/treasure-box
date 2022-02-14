package com.lifefighter.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_GENERIC
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.graphics.Rect
import android.os.*
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeInfo
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.channels.Channel
import kotlin.reflect.KClass

/**
 * @author xzp
 * @created on 2022/2/10.
 */
object AccessibilityServiceUtils {
    fun isAccessibilityServiceEnabled(
        context: Context,
        clazz: KClass<out AccessibilityService>,
        feedbackTypeFlags: Int = FEEDBACK_GENERIC
    ): Boolean {
        val manager =
            context.getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager
        val infoList = manager?.getEnabledAccessibilityServiceList(feedbackTypeFlags).orEmpty()
        for (info in infoList) {
            val serviceInfo = info.resolveInfo.serviceInfo
            if (serviceInfo.packageName == context.packageName && serviceInfo.name == clazz.qualifiedName) {
                return true
            }
        }
        return false
    }
}

fun AccessibilityNodeInfo.findAccessibilityNodeInfosByClassName(className: String): List<AccessibilityNodeInfo> {
    val resultList = mutableListOf<AccessibilityNodeInfo>()
    val nodeQueue = ArrayDeque<AccessibilityNodeInfo>()
    nodeQueue.addLast(this)
    while (nodeQueue.isNotEmpty()) {
        val nodeInfo = nodeQueue.removeFirst()
        if (nodeInfo.className?.toString() == className) {
            resultList.add(nodeInfo)
        }
        val childCount = nodeInfo.childCount
        if (childCount == 0) {
            continue
        }
        for (i in 0 until childCount) {
            val child = nodeInfo.getChild(i) ?: continue
            nodeQueue.addLast(child)
        }
    }
    return resultList
}

fun AccessibilityNodeInfo.findFirstAccessibilityNodeInfoByClassName(className: String): AccessibilityNodeInfo? {
    val nodeQueue = ArrayDeque<AccessibilityNodeInfo>()
    nodeQueue.addLast(this)
    while (nodeQueue.isNotEmpty()) {
        val nodeInfo = nodeQueue.removeFirst()
        if (nodeInfo.className?.toString() == className) {
            return nodeInfo
        }
        val childCount = nodeInfo.childCount
        if (childCount == 0) {
            continue
        }
        for (i in 0 until childCount) {
            val child = nodeInfo.getChild(i) ?: continue
            nodeQueue.addLast(child)
        }
    }
    return null
}

fun AccessibilityNodeInfo.findFirstAccessibilityNodeInfoByDescription(description: String): AccessibilityNodeInfo? {
    val nodeQueue = ArrayDeque<AccessibilityNodeInfo>()
    nodeQueue.addLast(this)
    while (nodeQueue.isNotEmpty()) {
        val nodeInfo = nodeQueue.removeFirst()
        if (nodeInfo.contentDescription?.toString() == description) {
            return nodeInfo
        }
        val childCount = nodeInfo.childCount
        if (childCount == 0) {
            continue
        }
        for (i in 0 until childCount) {
            val child = nodeInfo.getChild(i) ?: continue
            nodeQueue.addLast(child)
        }
    }
    return null
}


fun AccessibilityNodeInfo.findFirstAccessibilityNodeInfoByText(text: String): AccessibilityNodeInfo? {
    return this.findAccessibilityNodeInfosByText(text).firstOrNull()
}

fun AccessibilityNodeInfo.findFirstAccessibilityNodeInfoByViewId(viewId: String): AccessibilityNodeInfo? {
    return this.findAccessibilityNodeInfosByViewId(viewId).firstOrNull()
}

fun AccessibilityNodeInfo.getBoundsInScreen(): Rect {
    val bounds = Rect()
    this.getBoundsInScreen(bounds)
    return bounds
}

class ServiceConnectionWithMessenger : ServiceConnection {
    private var mService: Messenger? = null
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mService = Messenger(service)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mService = null
    }

    fun sendMessage(message: Message) {
        mService?.send(message)
    }
}


class MessengerBinder(lifecycleOwner: LifecycleOwner, callback: Handler.Callback) : Binder() {
    private val mThread = HandlerThread("MessengerBinder-${nextThreadNum()}")
    private var mHandler: Handler? = null

    init {
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                mThread.start()
                mHandler = Handler(mThread.looper, callback)
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                mHandler = null
                mThread.quitSafely()
            }
        })
    }

    companion object {
        var threadInitNumber = 0

        @Synchronized
        fun nextThreadNum(): Int {
            return threadInitNumber++
        }
    }
}

class ServiceConnectionWithService : ServiceConnection {
    private var mChannel = Channel<Service>(Channel.CONFLATED)
    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        tryOrNothing {
            val service = (binder as? ServiceBinder)?.service
            if (service != null) {
                mChannel.offer(service)
            }
        }

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        tryOrNothing {
            mChannel.close()
        }
    }

    suspend fun getService() = mChannel.receive()
}

class ServiceBinder(val service: Service) : Binder()