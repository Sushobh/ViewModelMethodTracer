package com.sushobh.methodlogger2

import LogItem
import LogViewItem
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.graphics.PixelFormat
import android.media.Image
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import java.time.Instant
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.math.log

fun onMethodLogged(className: String, methodName: String) {
    MethodLogger.onMethodLogged(LogItem(className, methodName))
}


@SuppressLint("StaticFieldLeak")
object MethodLogger {
    private val itemFlow = MutableSharedFlow<LogItem>(
        extraBufferCapacity = 5000,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private var context: Application? = null
    private var currentActivity: WeakReference<Activity> = WeakReference(null)
    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var adapter: SimpleTextAdapter? = null
    private var scope: CoroutineScope? = null
    private var recyclerView: RecyclerView? = null
    private var groupOfViews : Group? = null
    private var logListToggleButton : LogListToggleButton? = null


    fun setUpLogger(appContext: Application) {
        this.context = appContext
        context?.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {

            }

            override fun onActivityStarted(p0: Activity) {

            }

            override fun onActivityResumed(p0: Activity) {
                currentActivity = WeakReference(p0)
                onStartLogging()
            }

            override fun onActivityPaused(p0: Activity) {
                currentActivity = WeakReference(null)
                onStopLogging()
            }

            override fun onActivityStopped(p0: Activity) {

            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

            }

            override fun onActivityDestroyed(p0: Activity) {

            }
        })
    }

    private fun onStartLogging() {
        windowManager =
            currentActivity.get()!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        setUpLoggerView()
        setUpToggleView()
        observeItems()
    }

    private fun setUpLoggerView(){
        overlayView = LayoutInflater.from(context!!).inflate(R.layout.layout_list2, null)
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
        recyclerView = overlayView!!.findViewById<RecyclerView>(R.id.list_view)
        groupOfViews = overlayView!!.findViewById<Group>(R.id.view_group)

        recyclerView?.layoutManager = LinearLayoutManager(currentActivity.get()!!).apply {
            stackFromEnd = true
        }
        adapter = SimpleTextAdapter()
        recyclerView?.adapter = adapter
        recyclerView?.itemAnimator = FadeInItemAnimator()
        windowManager?.addView(overlayView, params)
    }

    private fun setUpToggleView(){
        val rootView = LayoutInflater.from(context!!).inflate(R.layout.view_toggle, null)
        logListToggleButton =  rootView as LogListToggleButton
        logListToggleButton?.setUpWithWindowManager(windowManager)

        logListToggleButton?.setOnClickListener {
            groupOfViews?.let {
                if (it.isVisible) {
                    logListToggleButton?.setImageResource(R.drawable.baseline_open_in_new_24)
                    it.visibility = View.GONE
                } else {
                    it.visibility = View.VISIBLE
                    logListToggleButton?.setImageResource(R.drawable.baseline_close_24)
                }
            }
        }
    }

    private fun observeItems() {
        scope = CoroutineScope(Dispatchers.Main)
        scope?.launch {
            itemFlow.groupedDebounce(1000,20).collect { newList : List<LogViewItem> ->
                scope?.launch {
                    withContext(Dispatchers.Main){
                        adapter?.submitList(newList)
                    }
                }
            }
        }
    }

    internal fun onMethodLogged(logItem: LogItem) {
        itemFlow.tryEmit(logItem)
    }

    private fun onStopLogging() {
        windowManager?.removeView(overlayView)
        windowManager?.removeView(logListToggleButton)
        overlayView = null
        logListToggleButton = null
        windowManager = null
        scope?.cancel()
    }
}

