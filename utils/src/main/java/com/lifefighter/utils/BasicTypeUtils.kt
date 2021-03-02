package com.lifefighter.utils

/**
 * @author xzp
 * @created on 2020/10/30.
 */

fun Boolean?.orFalse(): Boolean = this ?: false
fun Boolean?.orTrue(): Boolean = this ?: true


fun Int?.orZero(): Int = this ?: 0