package com.lifefighter.ocr

import android.content.res.AssetManager
import android.graphics.Bitmap
import com.lifefighter.utils.bg
import com.lifefighter.utils.ui

object OcrLibrary {
    private var mInited: Boolean = false

    init {
        System.loadLibrary("ocrlibrary")
    }

    suspend fun initLibrary(assetManager: AssetManager) = ui {
        if (mInited) {
            return@ui true
        }
        val inited = bg {
            init(assetManager)
        }
        mInited = inited
        return@ui inited
    }

    suspend fun detectBitmap(bitmap: Bitmap, useGpu: Boolean) = ui {
        if (mInited.not()) {
            return@ui null
        }
        return@ui bg {
            detect(bitmap, useGpu)
        }
    }

    external fun init(mgr: AssetManager): Boolean

    external fun detect(bitmap: Bitmap, use_gpu: Boolean): Array<OcrResult>?

}