package com.sushobh.methodlogger

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){

    fun randomMethodName1(){
        repeat(3) {
            randomMethodName3()
        }
    }

    fun randomMethodName2(){
         repeat(3) {
             randomMethodName4()
         }
    }

    fun randomMethodName3(){

    }

    fun randomMethodName4(){

    }

    fun randomMethodName5(){

    }

}