package com.lifefighter.widget.web

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

/**
 * @author xzp
 * @created on 2021/3/5.
 */
class ExWebView : WebView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    init {
        settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            allowContentAccess = true
            databaseEnabled = true
            domStorageEnabled = true
            setAppCacheEnabled(true)
            savePassword = false
            saveFormData = false
            useWideViewPort = true
        }
    }

    var userAgent: String?
        set(value) {
            settings.userAgentString = value
        }
        get() = settings.userAgentString
}