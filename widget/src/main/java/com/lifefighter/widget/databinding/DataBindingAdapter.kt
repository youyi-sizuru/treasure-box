package com.lifefighter.widget.databinding

import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter

/**
 * @author xzp
 * @created on 2020/10/23.
 */
object DataBindingAdapter {

    @JvmStatic
    @BindingAdapter("goneIf")
    fun goneIf(view: View, gone: Boolean) {
        view.visibility = if (gone) View.GONE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("goneIfNot")
    fun goneIfNot(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("invisibleIf")
    fun invisibleIf(view: View, invisible: Boolean) {
        view.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("invisibleIfNot")
    fun invisibleIfNot(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("android:inputType")
    fun setInputType(view: EditText, inputType: Int) {
        val selectionStart = view.selectionStart
        val selectionEnd = view.selectionEnd
        view.inputType = inputType
        if (selectionStart >= 0 && selectionEnd >= 0) {
            view.setSelection(selectionStart, selectionEnd)
        }
    }
}