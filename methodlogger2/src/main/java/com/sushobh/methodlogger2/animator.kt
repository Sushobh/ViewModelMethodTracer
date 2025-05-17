package com.sushobh.methodlogger2

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

internal class FadeInItemAnimator : DefaultItemAnimator() {
    override fun animateChange(oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder,
                               fromLeft: Int, fromTop: Int, toLeft: Int, toTop: Int): Boolean {
        newHolder.itemView.alpha = 0f
        newHolder.itemView.animate().alpha(1f).setDuration(300).start()
        return super.animateChange(oldHolder, newHolder, fromLeft, fromTop, toLeft, toTop)
    }
}