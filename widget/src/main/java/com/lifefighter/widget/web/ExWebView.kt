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
}