package com.sushobh.methodlogger2

import android.util.Log

fun onMethodLogged(className: String, methodName: String) {
    Log.i(className,methodName)
}