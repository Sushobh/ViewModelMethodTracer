package com.sushobh.methodlogger2

import android.util.Log

object LogReceiver {
    fun onMethodLogged(className: String, methodName: String) {
        Log.i(className,methodName)
    }
}