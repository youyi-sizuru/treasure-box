package com.lifefighter.widget.wallpaper

import android.graphics.Canvas
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder
import java.util.*

/**
 * 画布形式的壁纸服务
 * @author xzp
 * @created on 2021/12/25.
 */
abstract class CanvasWallpaperService : WallpaperService() {
    override fun onCreateEngine(): Engine {
        return CanvasEngine()
    }

    abstract fun onDraw(canvas: Canvas)

    inner class CanvasEngine : Engine() {
        private var cacheSurfaceHolder: SurfaceHolder? = null
        private var looperTimer: Timer? = null
        private val invalidateTask = object : TimerTask() {
            override fun run() {
                val holder = cacheSurfaceHolder ?: return
                val canvas = holder.lockCanvas()
                onDraw(canvas)
                holder.unlockCanvasAndPost(canvas)
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) {
                startInvalidate()
            } else {
                endInvalidate()
            }
        }

        private fun startInvalidate() {
            if (looperTimer == null) {
                looperTimer = Timer().also {
                    it.schedule(invalidateTask, 0, 50)
                }
            }
        }

        private fun endInvalidate() {
            looperTimer?.cancel()
            looperTimer?.purge()
            looperTimer = null
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

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            cacheSurfaceHolder = null
            endInvalidate()
        }
    }
}