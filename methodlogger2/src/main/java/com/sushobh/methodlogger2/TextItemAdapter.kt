package com.sushobh.methodlogger2

import LogViewItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import displayableClassName

internal class LogDiffCallback : DiffUtil.ItemCallback<LogViewItem>() {
    override fun areItemsTheSame(oldItem: LogViewItem, newItem: LogViewItem): Boolean {
        val result =  oldItem.className == newItem.className && oldItem.methodName == newItem.methodName

        return result
    }

    override fun areContentsTheSame(oldItem: LogViewItem, newItem: LogViewItem): Boolean {
        val result =  oldItem.count == newItem.count
        return result
    }

}

internal class SimpleTextAdapter2(private val items: List<LogViewItem>) :
    RecyclerView.Adapter<SimpleTextAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleTextAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return SimpleTextAdapter.ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SimpleTextAdapter.ViewHolder, position: Int) {
        holder.textView.text =
            "${items[position].displayableClassName()} ${items[position].methodName} (${items[position].count})"
    }
}


internal class SimpleTextAdapter :
    ListAdapter<LogViewItem, SimpleTextAdapter.ViewHolder>(LogDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text =
            "${getItem(position).className} ${getItem(position).methodName} (${getItem(position).count})"
    }
}
