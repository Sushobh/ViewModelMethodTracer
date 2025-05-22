package com.sushobh.methodlogger2.toggleview

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.ImageButton
import com.sushobh.methodlogger2.R
import com.sushobh.methodlogger2.common.LifecycleScopedView


class LogListToggleButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LifecycleScopedView(context, attrs, defStyleAttr), OnTouchListener {
    private var windowManager: WindowManager? = null
    private var windowManagerParams: WindowManager.LayoutParams? = null


    fun attachToWindowManger(windowManager: WindowManager?) {
        this.windowManager = windowManager
        inflate(context, R.layout.view_toggle, this)
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
        windowManager?.addView(this, windowManagerParams)
        setBackgroundColor(Color.GREEN)
        super.onAttach()
    }

    fun detachFromWindowManager() {
        super.onDetach()
        windowManager?.removeView(this)
    }

    init {
        setOnTouchListener(this)
    }

    fun setImageResource(resId: Int) {
        findViewById<ImageButton>(R.id.switch_button).setImageResource(resId)
    }

    private val gestureDetector =
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


    private fun togglePositioning(view: View) {
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

    override fun onViewCreated() {

    }

    override fun onViewDestroyed() {

    }

}
