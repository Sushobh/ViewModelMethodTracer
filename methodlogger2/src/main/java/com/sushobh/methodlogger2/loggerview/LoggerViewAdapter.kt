package com.sushobh.methodlogger2.loggerview

import LogViewItemType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sushobh.methodlogger2.R

internal class LogDiffCallback : DiffUtil.ItemCallback<LogViewItemType>() {
    override fun areItemsTheSame(oldItem: LogViewItemType, newItem: LogViewItemType): Boolean {
        if (oldItem is LogViewItemType.Header && newItem is LogViewItemType.Header) {
            return oldItem.logViewItemHeader.className == newItem.logViewItemHeader.className
        }
        if (oldItem is LogViewItemType.Item && newItem is LogViewItemType.Item) {
            return oldItem.logViewItem.className == newItem.logViewItem.className && oldItem.logViewItem.methodName == newItem.logViewItem.methodName
        }
        return false
    }

    override fun areContentsTheSame(oldItem: LogViewItemType, newItem: LogViewItemType): Boolean {
        if (oldItem is LogViewItemType.Header && newItem is LogViewItemType.Header) {
            val ans = oldItem.logViewItemHeader.className == newItem.logViewItemHeader.className
            return ans
        }
        if (oldItem is LogViewItemType.Item && newItem is LogViewItemType.Item) {
            val ans = oldItem.logViewItem.count == newItem.logViewItem.count
            return ans
        }
        return false
    }

}


internal class LoggerViewAdapter :
    ListAdapter<LogViewItemType, LoggerViewAdapter.ViewHolder>(LogDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view)
    }

    override fun getItemViewType(position: Int): Int {
        when (getItem(position)) {
            is LogViewItemType.Item -> return 0
            is LogViewItemType.Header -> return 1
            else -> throw Exception("Invalid item view type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        when (viewType) {
            0 -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_header, parent, false)
                return ViewHolder(itemView)
            }

            1 -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
                return ViewHolder(itemView)
            }
        }

        throw Exception("Invalid item view type")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is LogViewItemType.Header -> {
                holder.textView.text = item.logViewItemHeader.className
                return
            }

            is LogViewItemType.Item -> {
                holder.textView.text =
                    "${item.logViewItem.methodName} (${item.logViewItem.count})"
                return
            }

            else -> {

            }
        }
    }
}
