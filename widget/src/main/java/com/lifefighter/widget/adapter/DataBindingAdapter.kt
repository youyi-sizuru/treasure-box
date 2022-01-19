package com.lifefighter.widget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseBinderAdapter
import com.chad.library.adapter.base.binder.QuickDataBindingItemBinder

/**
 * @author xzp
 * @created on 2020/11/19.
 */
class DataBindingAdapter : BaseBinderAdapter() {
    val isEmpty
        get() = data.isEmpty()

    inline fun <reified T : Any> addItemBinder(baseItemBinder: ViewItemBinder<T, *>): BaseBinderAdapter {
        return addItemBinder(baseItemBinder, baseItemBinder.callback)
    }
}

class ViewItemBinder<T, DB : ViewDataBinding>(
    private val layoutIdProvider: (viewType: Int) -> Int,
    private val lifecycleOwner: LifecycleOwner,
    private val customConvert: ((DB, T) -> Unit)? = null,
    val callback: DiffUtil.ItemCallback<T>? = null
) : QuickDataBindingItemBinder<T, DB>() {

    private val childClickMap = hashMapOf<Int, (DB, T, position: Int) -> Unit>()
    var onItemClick: ((DB, T, position: Int) -> Unit)? = null

    constructor(
        layoutId: Int,
        lifecycleOwner: LifecycleOwner,
        customConvert: ((DB, T) -> Unit)? = null,
        callback: DiffUtil.ItemCallback<T>? = null
    ) : this(
        layoutIdProvider = { layoutId },
        lifecycleOwner = lifecycleOwner,
        customConvert = customConvert,
        callback = callback
    )

    override fun convert(holder: BinderDataBindingHolder<DB>, data: T) {
        holder.dataBinding.setVariable(DataBindingHelper.DEFAULT_BINDING_VARIABLE, data)
        holder.dataBinding.executePendingBindings()
        customConvert?.invoke(holder.dataBinding, data)
    }

    override fun onChildClick(
        holder: BinderDataBindingHolder<DB>,
        view: View,
        data: T,
        position: Int
    ) {
        childClickMap[view.id]?.invoke(holder.dataBinding, data, position)
    }

    override fun onClick(holder: BinderDataBindingHolder<DB>, view: View, data: T, position: Int) {
        onItemClick?.invoke(holder.dataBinding, data, position)
    }

    fun addChildClick(@IdRes id: Int, click: (DB, T, position: Int) -> Unit) {
        childClickMap[id] = click
        addChildClickViewIds(id)
    }

    override fun onCreateDataBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): DB {
        return DataBindingUtil.inflate<DB>(
            layoutInflater,
            layoutIdProvider.invoke(viewType),
            parent,
            false
        ).also {
            it.lifecycleOwner = lifecycleOwner
        }
    }
}

object DataBindingHelper {
    var DEFAULT_BINDING_VARIABLE: Int = -1
}