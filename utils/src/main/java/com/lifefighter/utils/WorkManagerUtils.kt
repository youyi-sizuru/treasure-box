package com.lifefighter.utils

import android.content.Context
import androidx.work.WorkManager

/**
 * @author xzp
 * @created on 2021/3/16.
 */
object WorkManagerUtils {
    /**
     * 判断WorkManager中指定的tag是否全部结束了
     */
    fun isRequestFinishedByTag(context: Context, tag: String): Boolean {
        return tryOrNull {
            WorkManager.getInstance(context).getWorkInfosByTag(tag).get()?.none {
                !it.state.isFinished
            }
        }.orTrue()
    }
}