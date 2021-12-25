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
    private var canvasEngine: CanvasEngine? = null
    override fun onCreateEngine(): Engine {
        return CanvasEngine().also {
            canvasEngine = it
        }
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

        override fun onSurfaceCreated(holder: SurfaceHolder) {
            cacheSurfaceHolder = holder
            looperTimer = Timer().also {
                it.schedule(invalidateTask, 0, 100)
            }

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
            looperTimer?.cancel()
            looperTimer?.purge()
            looperTimer = null
        }
    }
}