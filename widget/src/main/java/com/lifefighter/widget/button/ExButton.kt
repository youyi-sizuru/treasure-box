package com.lifefighter.widget.button

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

/**
 * @author xzp
 * @created on 2020/10/22.
 */
class ExButton : MaterialButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
}