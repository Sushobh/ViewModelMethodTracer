package com.sushobh.methodlogger2.loggerview

import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sushobh.methodlogger2.R
import com.sushobh.methodlogger2.common.LifecycleScopedView
import com.sushobh.methodlogger2.common.getViewModel
import kotlinx.coroutines.launch

class LoggerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LifecycleScopedView(context, attrs, defStyleAttr) {

    private var windowManager: WindowManager? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: LoggerViewAdapter = LoggerViewAdapter()
    private val viewModel: LoggerViewModel by lazy {
        getViewModel()
    }
    private var isEnabled = false

    override fun isEnabled() = isEnabled

    fun attachToWindowManager(windowManager: WindowManager) {
        this.windowManager = windowManager
        inflate(context, R.layout.layout_list2, this)
        visibility = View.GONE
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT, // Thin view height
            WindowManager.LayoutParams.TYPE_APPLICATION, // App-local only
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        windowManager.addView(this, params)
        super.onAttach()
    }

    fun enableView() {
        with(layoutParams as WindowManager.LayoutParams) {
            flags = flags and WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE.inv()
            windowManager?.updateViewLayout(this@LoggerView, this)
        }
        isEnabled = true
        visibility = View.VISIBLE
    }

    fun disableView() {
        with(layoutParams as WindowManager.LayoutParams) {
            flags = flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            windowManager?.updateViewLayout(this@LoggerView, this)
        }
        isEnabled = false
        visibility = View.GONE
    }

    fun detachFromWindowManager() {
        super.onDetach()
        windowManager?.removeView(this)
    }

    override fun onViewCreated() {
        recyclerView = findViewById(R.id.list_view)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = adapter
        recyclerView?.itemAnimator = FadeInItemAnimator()
        lifecycleScope.launch {
            viewModel.groupedLogItems.collect { newList ->
                adapter.submitList(newList)
            }
        }
    }

    override fun onViewDestroyed() {

    }

}