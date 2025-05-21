package com.sushobh.methodlogger2

import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager


class LogListToggleButton : androidx.appcompat.widget.AppCompatImageButton, OnTouchListener {
    private var windowManager: WindowManager? = null
    private var windowManagerParams: WindowManager.LayoutParams? = null

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }

    init {
        setOnTouchListener(this)
    }

    val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                togglePositioning(this@LogListToggleButton)
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                performClick()
                return true
            }
        })


    private fun togglePlacement() {
        val windowManagerParams = (layoutParams as WindowManager.LayoutParams)
        val gravity = windowManagerParams.gravity
        when {
            gravity and Gravity.START == Gravity.START -> {
                windowManagerParams.gravity = Gravity.END
                windowManager?.updateViewLayout(this, windowManagerParams)
            }

            gravity and Gravity.END == Gravity.END -> {
                windowManagerParams.gravity = Gravity.START
                windowManager?.updateViewLayout(this, windowManagerParams)
            }
        }
    }

    fun setUpWithWindowManager(windowManager: WindowManager?) {
        this.windowManager = windowManager
        windowManagerParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, // Thin view height
            WindowManager.LayoutParams.TYPE_APPLICATION, // App-local only
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        windowManagerParams?.gravity = Gravity.START
        windowManager?.addView(rootView, windowManagerParams)
    }

    fun togglePositioning(view: View) {
        val layoutParams = view.layoutParams
        if (layoutParams is WindowManager.LayoutParams) {
            val g = layoutParams.gravity

            layoutParams.gravity = when (g) {
                Gravity.START -> Gravity.TOP or Gravity.START
                Gravity.TOP or Gravity.START -> Gravity.TOP or Gravity.END
                Gravity.TOP or Gravity.END -> Gravity.END
                Gravity.END -> Gravity.BOTTOM or Gravity.END
                Gravity.BOTTOM or Gravity.END -> Gravity.BOTTOM or Gravity.START
                Gravity.BOTTOM or Gravity.START -> Gravity.START
                else -> Gravity.START // fallback
            }

            view.layoutParams = layoutParams
            windowManager?.updateViewLayout(this, layoutParams)
        }
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        p1?.let { gestureDetector.onTouchEvent(it) }
        return true
    }

}
