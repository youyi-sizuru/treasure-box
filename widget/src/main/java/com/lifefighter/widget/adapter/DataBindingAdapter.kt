package com.lifefighter.widget.adapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author xzp
 * @created on 2020/11/19.
 */
class DataBindingAdapter<T, V : ViewDataBinding>(
    layoutId: Int,
    private val lifecycleOwner: LifecycleOwner? = null,
    val bind: ((itemBinding: V, item: T) -> Unit)? = null
) :
    BaseQuickAdapter<T, BaseViewHolder>(layoutId) {
    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<V>(viewHolder.itemView)?.also {
            it.lifecycleOwner = lifecycleOwner
        }
    }

    override fun convert(holder: BaseViewHolder, item: T) {
        DataBindingUtil.getBinding<V>(holder.itemView)?.also {
            bind?.invoke(it, item)
            it.executePendingBindings()
        }
    }

    val isEmpty
        get() = data.isEmpty()
}