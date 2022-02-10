package com.lifefighter.utils

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo.FEEDBACK_GENERIC
import android.content.Context
import android.graphics.Rect
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeInfo
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

fun AccessibilityNodeInfo.getBoundsInScreen(): Rect {
    val bounds = Rect()
    this.getBoundsInScreen(bounds)
    return bounds
}