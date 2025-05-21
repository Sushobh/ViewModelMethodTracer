package com.sushobh.methodlogger

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    fun loginRandomMethodName1() {
        repeat(3) {
            loginRandomMethodName3()
        }
    }

    fun loginRandomMethodName2() {
        repeat(3) {
            loginRandomMethodName4()
        }
    }

    fun loginRandomMethodName3() {

    }

    fun loginRandomMethodName4() {

    }

    fun loginRandomMethodName5() {

    }

}