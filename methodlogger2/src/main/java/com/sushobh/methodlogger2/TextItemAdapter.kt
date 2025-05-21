package com.sushobh.methodlogger2

import LogViewItem
import LogViewItemHeader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

internal class LogDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem is LogViewItemHeader && newItem is LogViewItemHeader) {
            return oldItem.className == newItem.className
        }
        if (oldItem is LogViewItem && newItem is LogViewItem) {
            return oldItem.className == newItem.className && oldItem.methodName == newItem.methodName
        }

        return false
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        if (oldItem is LogViewItemHeader && newItem is LogViewItemHeader) {
            return oldItem.className == newItem.className
        }
        if (oldItem is LogViewItem && newItem is LogViewItem) {
            return oldItem.count == newItem.count
        }
        return false
    }

}


internal class SimpleTextAdapter :
    ListAdapter<Any, SimpleTextAdapter.ViewHolder>(LogDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view)
    }

    override fun getItemViewType(position: Int): Int {
        when (getItem(position)) {
            is LogViewItemHeader -> return 0
            is LogViewItem -> return 1
        }
        throw Exception("Invalid item view type")
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
        val item = getItem(position)

        when (item) {
            is LogViewItemHeader -> {
                holder.textView.text = item.className
                return
            }

            is LogViewItem -> {
                holder.textView.text =
                    "${item.methodName} (${item.count})"
                return
            }
        }
    }
}
