package com.lifefighter.widget.wallpaper

import android.graphics.Canvas
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import com.lifefighter.utils.tryOrNothing
import java.util.*

/**
 * 画布形式的壁纸服务
 * @author xzp
 * @created on 2021/12/25.
 */
abstract class CanvasWallpaperService : WallpaperService(), CanvasPainter {
    override fun onCreateEngine(): Engine {
        return CanvasEngine(this)
    }

    private inner class CanvasEngine(private val painter: CanvasPainter) : Engine() {
        private var cacheSurfaceHolder: SurfaceHolder? = null
        private var looperTimer: Timer? = null
        private val invalidateTask = object : TimerTask() {
            override fun run() {
                val holder = cacheSurfaceHolder ?: return
                tryOrNothing {
                    val canvas = holder.lockCanvas()
                    painter.onDraw(canvas)
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }


        private fun startInvalidate() {
            if (looperTimer == null) {
                looperTimer = Timer().also {
                    it.schedule(invalidateTask, 0, 20)
                }
            }
        }

        private fun endInvalidate() {
            looperTimer?.cancel()
            looperTimer?.purge()
            looperTimer = null
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) {
                painter.onResume()
            } else {
                painter.onPause()
            }
        }

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            cacheSurfaceHolder = holder
            startInvalidate()
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {
            cacheSurfaceHolder = holder
        }

        override fun onOffsetsChanged(
            xOffset: Float,
            yOffset: Float,
            xOffsetStep: Float,
            yOffsetStep: Float,
            xPixelOffset: Int,
            yPixelOffset: Int
        ) {
            if (xOffsetStep != 0f && yOffsetStep != 0f) {
                painter.onOffset(xOffset.div(xOffsetStep), yOffset.div(yOffsetStep))
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            cacheSurfaceHolder = null
            endInvalidate()
        }
    }
}

interface CanvasPainter {
    fun onDraw(canvas: Canvas) {}

    /**
     * 屏幕偏移时触发
     * [xOffset] 主屏幕x轴的偏移量，假设主屏幕有5个，现在停留在第3个，则为2
     * [yOffset] 主屏幕y轴的偏移量，解释与xOffset同理
     */
    fun onOffset(xOffset: Float, yOffset: Float) {}

    fun onResume() {}

    fun onPause() {}


}