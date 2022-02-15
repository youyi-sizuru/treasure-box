package com.lifefighter.ocr

import android.content.res.AssetManager
import android.graphics.Bitmap

object OcrLibrary {
    init {
        System.loadLibrary("ocrlibrary")
    }

    external fun init(mgr: AssetManager): Boolean

    external fun detect(bitmap: Bitmap, use_gpu: Boolean): Array<OcrResult>?

}