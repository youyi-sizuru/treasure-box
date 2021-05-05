package com.lifefighter.widget.text

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckedTextView

open class ExCheckedText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.checkedTextViewStyle
) : AppCompatCheckedTextView(context, attrs, defStyleAttr)