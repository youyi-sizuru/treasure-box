package com.lifefighter.widget.bar

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import com.lifefighter.utils.getRootActivity
import com.lifefighter.widget.R

/**
 * @author xzp
 * @created on 2021/3/8.
 */
class ExToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.toolbarStyle
) : Toolbar(context, attrs, defStyleAttr) {
    init {
        if (attrs != null) {
            val typeArray =
                context.obtainStyledAttributes(attrs, R.styleable.ExToolbar, defStyleAttr, 0)
            if (typeArray.getBoolean(R.styleable.ExToolbar_navigationToFinish, false)) {
                setNavigationOnClickListener {
                    context.getRootActivity()?.finish()
                }
            }
            typeArray.recycle()
        }
    }
}