package com.lifefighter.widget.text

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import com.lifefighter.utils.StringUtils
import com.lifefighter.widget.image.ExImageView

/**
 * @author xzp
 * @created on 2020/10/29.
 */
class CleanEditView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ExImageView(context, attrs, defStyleAttr), TextWatcher {

    var editText: EditText? = null
        set(value) {
            field?.removeTextChangedListener(this)
            field = value
            if (value == null) {
                setOnClickListener(null)
                visibility = View.INVISIBLE
            } else {
                setOnClickListener {
                    value.setText(StringUtils.EMPTY)
                }
                val text = value.text.toString()
                visibility = if (text.isNotEmpty()) View.VISIBLE else View.INVISIBLE
                value.addTextChangedListener(this)
            }
        }

    override fun afterTextChanged(s: Editable?) {
        val text = s?.toString()
        visibility = if (!text.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}