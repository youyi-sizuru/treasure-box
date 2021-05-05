package com.lifefighter.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lifefighter.base.Const.BUNDLE_NAME
import kotlin.reflect.KClass

/**
 * @author xzp
 * @created on 2020/10/19.
 */

abstract class BaseFragment<T : ViewDataBinding> : Fragment(), LifecycleBinder<T> {
    override val rootActivity: BaseActivity<*>
        get() = this.activity as BaseActivity<*>
    private lateinit var innerViewBinding: T
    override val viewBinding: T
        get() = innerViewBinding
    override val currentFragmentManager: FragmentManager
        get() = childFragmentManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        innerViewBinding = createViewBinding(container)
        return innerViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onLifecycleInit(savedInstanceState)
        registerEventBus()
    }

    override fun onStart() {
        super.onStart()
        onLifecycleStart()
    }

    override fun onStop() {
        super.onStop()
        onLifecycleStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterEventBus()
        onLifecycleDestroy()
    }

    override fun finish() {
        rootActivity.finish()
    }

    override fun route(kClass: KClass<*>, parcelable: Parcelable?, requestCode: Int?) {
        val intent = Intent(this.rootActivity, kClass.java)
        if (parcelable != null) {
            intent.putExtra(BUNDLE_NAME, parcelable)
        }
        if (requestCode == null) {
            startActivity(intent)
        } else {
            startActivityForResult(intent, requestCode)
        }
    }

    override fun <T : Parcelable> getBundleNullable(): T? {
        return arguments?.getParcelable(BUNDLE_NAME)
    }

}

abstract class BaseDialogFragment<T : ViewDataBinding> : AppCompatDialogFragment(),
    LifecycleBinder<T> {
    override val rootActivity: BaseActivity<*>
        get() = this.activity as BaseActivity<*>
    private lateinit var innerViewBinding: T
    override val viewBinding: T
        get() = innerViewBinding
    override val currentFragmentManager: FragmentManager
        get() = childFragmentManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        innerViewBinding = createViewBinding(container)
        return innerViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onLifecycleInit(savedInstanceState)
        registerEventBus()
    }

    override fun onStart() {
        super.onStart()
        onLifecycleStart()
    }

    override fun onStop() {
        super.onStop()
        onLifecycleStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterEventBus()
        onLifecycleDestroy()
    }

    override fun finish() {
        rootActivity.finish()
    }

    override fun route(kClass: KClass<*>, parcelable: Parcelable?, requestCode: Int?) {
        val intent = Intent(this.rootActivity, kClass.java)
        if (parcelable != null) {
            intent.putExtra("bundle", parcelable)
        }
        if (requestCode == null) {
            startActivity(intent)
        } else {
            startActivityForResult(intent, requestCode)
        }
    }
}

private class BottomDialog(context: Context, theme: Int) : BottomSheetDialog(context, theme) {
    override fun setContentView(layoutResId: Int) {
        super.setContentView(layoutResId)
        updateBehavior()
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        updateBehavior()
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        updateBehavior()
    }

    private fun updateBehavior() {
        val layoutParams =
            findViewById<View>(R.id.design_bottom_sheet)?.layoutParams as? CoordinatorLayout.LayoutParams?
        if (layoutParams != null) {
            layoutParams.height = (context.resources.displayMetrics.heightPixels * 0.4f).toInt()
            (layoutParams.behavior as? BottomSheetBehavior)?.let {
                it.state = BottomSheetBehavior.STATE_EXPANDED
                it.skipCollapsed = true
                it.isDraggable = false
            }
        }
    }
}

abstract class BaseBottomDialogFragment<T : ViewDataBinding> : BottomSheetDialogFragment(),
    LifecycleBinder<T> {
    override val rootActivity: BaseActivity<*>
        get() = this.activity as BaseActivity<*>
    private lateinit var innerViewBinding: T
    override val viewBinding: T
        get() = innerViewBinding
    override val currentFragmentManager: FragmentManager
        get() = childFragmentManager

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomDialog(rootContext, theme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        innerViewBinding = createViewBinding(container)
        return innerViewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onLifecycleInit(savedInstanceState)
        registerEventBus()
    }

    override fun onStart() {
        super.onStart()
        onLifecycleStart()
    }

    override fun onStop() {
        super.onStop()
        onLifecycleStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterEventBus()
        onLifecycleDestroy()
    }

    override fun finish() {
        rootActivity.finish()
    }

    override fun route(kClass: KClass<*>, parcelable: Parcelable?, requestCode: Int?) {
        val intent = Intent(this.rootActivity, kClass.java)
        if (parcelable != null) {
            intent.putExtra("bundle", parcelable)
        }
        if (requestCode == null) {
            startActivity(intent)
        } else {
            startActivityForResult(intent, requestCode)
        }
    }

    override fun <T : Parcelable> getBundleNullable(): T? {
        return arguments?.getParcelable("bundle")
    }
}

fun Fragment.putBundle(parcelable: Parcelable) {
    this.arguments = Bundle().apply {
        putParcelable("bundle", parcelable)
    }
}