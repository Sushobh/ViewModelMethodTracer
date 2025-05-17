package com.sushobh.methodlogger2

import LogItem
import LogViewItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal fun Flow<LogItem>.groupedDebounce(durationMs: Long, size: Int = 20) =
    channelFlow {
        val items = DoublyLinkedList<LogViewItem>()
        launch {
            while (isActive) {
                delay(durationMs)
                send(items.toList().map { it.copy() })
            }
        }

        collect { item ->
            val existingItem =
                items.find { it.className == item.className && it.methodName == item.methodName }
            if (existingItem != null) {
                existingItem.value.count++
                items.removeNode(existingItem)
                items.addLast(existingItem.value)
            } else {
                if (items.getSize() > size) {
                    items.removeFirst()
                }
                items.addLast(LogViewItem(item.className, item.methodName, 1))
            }
        }
    }