package com.lifefighter.widget.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

/**
 * @author xzp
 * @created on 2020/11/19.
 */
class SpaceDrawable(val width: Int = 0, val height: Int = 0) : Drawable() {
    override fun draw(canvas: Canvas) {
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getIntrinsicHeight(): Int {
        return height
    }

    override fun getIntrinsicWidth(): Int {
        return width
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }
}