package com.lifefighter.base

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.lifefighter.base.Const.BUNDLE_NAME
import org.koin.androidx.scope.ScopeActivity
import kotlin.reflect.KClass

/**
 * @author xzp
 * @created on 2020/10/19.
 */
abstract class BaseActivity<T : ViewDataBinding> : ScopeActivity(initialiseScope = false),
    LifecycleBinder<T> {
    override val rootActivity: BaseActivity<*>
        get() = this
    private lateinit var innerViewBinding: T
    override val viewBinding: T
        get() = innerViewBinding

    override val currentFragmentManager: FragmentManager
        get() = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        innerViewBinding = createViewBinding()
        setContentView(innerViewBinding.root)
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterEventBus()
        onLifecycleDestroy()
    }

    override fun route(kClass: KClass<*>, parcelable: Parcelable?, requestCode: Int?) {
        val intent = Intent(this, kClass.java)
        if (parcelable != null) {
            intent.putExtra(BUNDLE_NAME, parcelable)
        }
        if (requestCode == null) {
            startActivity(intent)
        } else {
            startActivityForResult(intent, requestCode)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        this.intent = intent
    }

    override fun <T : Parcelable> getBundleNullable(): T? {
        return intent.getParcelableExtra(BUNDLE_NAME)
    }

    fun setParcelableResult(parcelable: Parcelable) {
        setResult(RESULT_OK, Intent().apply {
            putExtra(BUNDLE_NAME, parcelable)
        })
    }
}