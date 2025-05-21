package com.sushobh.methodlogger2.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

inline fun <reified T : ViewModel> ViewModelStoreOwner.getViewModel(
    factory: ViewModelProvider.Factory? = null
): T {
    // If a factory is provided, use it; otherwise, use the default ViewModelProvider constructor
    return if (factory != null) {
        ViewModelProvider(this, factory).get(T::class.java)
    } else {
        ViewModelProvider(this).get(T::class.java)
    }
}