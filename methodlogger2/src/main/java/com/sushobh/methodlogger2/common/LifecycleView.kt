package com.sushobh.methodlogger2.common

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

abstract class WindowManagerScopedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ViewModelStoreOwner, LifecycleOwner {

    private val lifecycleRegistry = LifecycleRegistry(this)
    private var customViewModelStore: ViewModelStore? = null

    override val viewModelStore: ViewModelStore
        get() = customViewModelStore
            ?: throw IllegalStateException("(ViewModel store not intialized")

    init {
        // We start in INITIALIZED state, you can transition in attach
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    private val lifecycleObserver = LifecycleEventObserver { source, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                customViewModelStore = ViewModelStore()
            }

            Lifecycle.Event.ON_DESTROY -> {
                customViewModelStore?.clear()
                customViewModelStore = null
            }

            else -> {

            }
        }
    }

    open fun onAttach() {
        lifecycle.addObserver(lifecycleObserver)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }


    open fun onDetach() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        lifecycle.removeObserver(lifecycleObserver)
    }
}
