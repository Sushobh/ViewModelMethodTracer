package com.sushobh.methodlogger2

import LogItem
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.sushobh.methodlogger2.loggerview.LoggerView
import com.sushobh.methodlogger2.toggleview.LogListToggleButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.lang.ref.WeakReference

fun onMethodLogged(className: String, methodName: String) {
    MethodLogger.onMethodLogged(LogItem(className, methodName))
}


@SuppressLint("StaticFieldLeak")
object MethodLogger {
    internal val itemFlow = MutableSharedFlow<LogItem>(
        extraBufferCapacity = 5000,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private var context: Application? = null
    private var currentActivity: WeakReference<Activity> = WeakReference(null)
    private var windowManager: WindowManager? = null
    private var scope: CoroutineScope? = null
    private var logListToggleButton: LogListToggleButton? = null
    private var loggerView: LoggerView? = null

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
        loggerView = LoggerView(currentActivity.get()!!)
        logListToggleButton = LogListToggleButton(currentActivity.get()!!)
        loggerView?.attachToWindowManager(windowManager!!)
        logListToggleButton?.attachToWindowManger(windowManager)
        setUpToggleView()
    }



    private fun setUpToggleView() {
        logListToggleButton?.setOnClickListener {
            loggerView?.let { lgView ->
                if (lgView.isEnabled()) {
                    logListToggleButton?.setImageResource(R.drawable.baseline_open_in_new_24)
                    lgView.disableView()
                } else {
                    logListToggleButton?.setImageResource(R.drawable.baseline_close_24)
                    lgView.enableView()
                }
            }
        }
    }



    internal fun onMethodLogged(logItem: LogItem) {
        itemFlow.tryEmit(logItem)
    }

    private fun onStopLogging() {
        loggerView?.detachFromWindowManager()
        logListToggleButton?.detachFromWindowManager()
        logListToggleButton = null
        windowManager = null
        scope?.cancel()
    }
}

