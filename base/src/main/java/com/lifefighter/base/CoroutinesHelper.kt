package com.lifefighter.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatDialog
import kotlinx.coroutines.Job

/**
 * @author xzp
 * @created on 2021/3/15.
 */
object CoroutinesHelper {
    var onCreateLoadingDialog: (LifecycleBinder<*>) -> Dialog = { binder ->
        DefaultLoadingDialog(binder.rootContext)
    }
}

internal class DefaultLoadingDialog(context: Context) :
    AppCompatDialog(context, R.style.DefaultLoadingDialogStyle) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.default_loading_dialog)
    }
}


@MainThread
fun Job.withLoadingDialog(binder: LifecycleBinder<*>): Dialog {
    val dialog = CoroutinesHelper.onCreateLoadingDialog.invoke(binder)
    if (isActive) {
        dialog.show()
        this.invokeOnCompletion {
            dialog.dismiss()
        }
    }
    return dialog
}