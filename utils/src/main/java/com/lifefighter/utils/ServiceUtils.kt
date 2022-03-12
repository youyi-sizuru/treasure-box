package com.lifefighter.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_GENERIC
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.graphics.Rect
import android.os.Binder
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeInfo
import kotlinx.coroutines.channels.Channel
import java.util.*
import kotlin.collections.ArrayDeque
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
    private val messageQueue = Collections.synchronizedList<Message>(mutableListOf())
    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        mService = Messenger(service).also {
            tryOrNothing {
                while (true) {
                    val message = messageQueue.removeAt(0)
                    it.send(message)
                }
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mService = null
    }

    fun sendMessage(message: Message) {
        if (mService == null) {
            messageQueue.add(message)
        } else {
            mService?.send(message)
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