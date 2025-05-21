package com.sushobh.methodlogger

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    fun mainRandomMethodName1() {
        repeat(3) {
            mainRandomMethodName3()
        }
    }

    fun mainRandomMethodName2() {
        repeat(3) {
            mainRandomMethodName4()
        }
    }

    fun mainRandomMethodName3() {

    }

    fun mainRandomMethodName4() {

    }

    fun mainRandomMethodName5() {

    }

}