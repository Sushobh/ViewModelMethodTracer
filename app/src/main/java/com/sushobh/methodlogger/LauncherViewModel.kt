package com.sushobh.methodlogger

import androidx.lifecycle.ViewModel

class LauncherViewModel : ViewModel() {
    fun launchRandomMethodName1() {
        repeat(3) {
            launchRandomMethodName3()
        }
    }

    fun launchRandomMethodName2() {
        repeat(3) {
            launchRandomMethodName4()
        }
    }

    fun launchRandomMethodName3() {

    }

    fun launchRandomMethodName4() {

    }

    fun launchRandomMethodName5() {

    }

}