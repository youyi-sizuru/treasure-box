package com.lifefighter.utils

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.meta.SubscriberInfoIndex
import kotlin.reflect.KClass

/**
 * @author xzp
 * @created on 2018/7/27.
 */
object EventBusManager {
    private lateinit var eventBus: EventBus

    fun init(vararg indexes: SubscriberInfoIndex) {
        val eventBusBuilder = EventBus.builder()
        indexes.forEach {
            eventBusBuilder.addIndex(it)
        }
        eventBus = eventBusBuilder.installDefaultEventBus()
    }

    fun post(event: Any) = eventBus.post(event)

    fun register(subscriber: Any) = eventBus.register(subscriber)

    fun unregister(subscriber: Any) = eventBus.unregister(subscriber)

    fun postSticky(event: Any) = eventBus.postSticky(event)

    fun removeStickyEvent(event: Any) = eventBus.removeStickyEvent(event)

    fun removeAllStickyEvents() = eventBus.removeAllStickyEvents()

    fun removeStickyEvent(eventType: KClass<*>) = eventBus.removeStickyEvent(eventType.java)
}