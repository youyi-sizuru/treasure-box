package com.lifefighter.utils

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @author xzp
 * @created on 2020/11/3.
 */
object FragmentTransactionUtils {
    var defaultEnterAnim: Int = 0
        private set
    var defaultExitAnim: Int = 0
        private set
    var defaultPopEnterAnim: Int = 0
        private set
    var defaultPopExitAnim: Int = 0
        private set

    fun setDefaultAnimations(
        @AnimatorRes @AnimRes enter: Int,
        @AnimatorRes @AnimRes exit: Int, @AnimatorRes @AnimRes popEnter: Int,
        @AnimatorRes @AnimRes popExit: Int
    ) {
        defaultEnterAnim = enter
        defaultExitAnim = exit
        defaultPopEnterAnim = popEnter
        defaultPopExitAnim = popExit
    }
}

class FragmentTransactionBuilder private constructor(fragmentManager: FragmentManager) {
    private val fragmentTransaction = fragmentManager.beginTransaction()


    fun replace(@IdRes containerViewId: Int, fragment: Fragment, tag: String? = null) = apply {
        fragmentTransaction.replace(containerViewId, fragment, tag)
    }

    fun add(@IdRes containerViewId: Int, fragment: Fragment, tag: String? = null) = apply {
        fragmentTransaction.add(containerViewId, fragment, tag)
    }

    fun addToBackStack(name: String? = null) = apply {
        fragmentTransaction.addToBackStack(name)
    }

    fun noAnimation() = apply {
        fragmentTransaction.setCustomAnimations(0, 0, 0, 0)
    }

    /**
     * use it before add or replace op.
     */
    fun useDefaultAnimation() = apply {
        fragmentTransaction.setCustomAnimations(
            FragmentTransactionUtils.defaultEnterAnim,
            FragmentTransactionUtils.defaultExitAnim,
            FragmentTransactionUtils.defaultPopEnterAnim,
            FragmentTransactionUtils.defaultPopExitAnim
        )
    }

    fun build(allowingStateLoss: Boolean = false) {
        if (allowingStateLoss) {
            fragmentTransaction.commitAllowingStateLoss()
        } else {
            fragmentTransaction.commit()
        }
    }

    companion object {
        fun with(fragmentManager: FragmentManager): FragmentTransactionBuilder =
            FragmentTransactionBuilder(fragmentManager)
    }
}