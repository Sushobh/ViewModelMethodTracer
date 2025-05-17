package com.sushobh.methodlogger2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
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

fun onMethodLogged(className: String, methodName: String) {
    MethodLogger.onMethodLogged(LogItem(className, methodName))
}

internal data class LogViewItem(
    override var className: String,
    override var methodName: String,
    var count: Int
) : LogItem(className, methodName)

internal fun LogViewItem.displayableClassName() : String {
    return className.split("/").last()
}
open class LogItem(
    open val className: String,
    open val methodName: String,
    open val loggedTime: Instant = Instant.now()
)

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


    fun setUpLogger(appContext: Application) {
        this.context = appContext
        context?.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {

            }

            override fun onActivityStarted(p0: Activity) {
                currentActivity = WeakReference(p0)
                onStartLogging()
            }

            override fun onActivityResumed(p0: Activity) {

            }

            override fun onActivityPaused(p0: Activity) {

            }

            override fun onActivityStopped(p0: Activity) {
                currentActivity = WeakReference(null)
                onStopLogging()
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
        recyclerView?.layoutManager = LinearLayoutManager(currentActivity.get()!!).apply {
            stackFromEnd = true
        }
        adapter = SimpleTextAdapter()
        recyclerView?.adapter = adapter

        windowManager?.addView(overlayView, params)
        observeItems()
    }

    private fun observeItems() {
        scope = CoroutineScope(Dispatchers.Main)
        scope?.launch {
            itemFlow.groupedDebounce(1000).collect { newList ->
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
        overlayView = null
        windowManager = null
        recyclerView = null
        scope?.cancel()
    }
}

private fun Flow<LogItem>.groupedDebounce(durationMs: Long, size: Int = 20) =
    channelFlow {
        val items = ArrayDeque<LogViewItem>()
        launch {
            while (isActive) {
                delay(durationMs)
                send(items.map { it.copy() })
            }
        }

        collect { item ->
            val existingItem =
                items.find { it.className == item.className && it.methodName == item.methodName }
            if (existingItem != null) {
                existingItem.count += 1
            } else {
                if (items.size > size) {
                    items.removeFirst()
                }
                items.addLast(LogViewItem(item.className, item.methodName, 1))
            }
        }
    }