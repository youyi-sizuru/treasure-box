package com.lifefighter.widget.scroll

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView

/**
 * @author xzp
 * @created on 2020/11/9.
 */
class ExScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {
    init {
        overScrollMode = OVER_SCROLL_NEVER
    }
}