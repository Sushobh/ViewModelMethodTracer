package com.sushobh.methodlogger2

import LogItem
import LogViewItem
import LogViewItemHeader
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal fun Flow<LogItem>.groupedDebounce(durationMs: Long, size: Int = 20) =
    channelFlow {
        val logItemHashMap = HashMap<String, DoublyLinkedList<LogViewItem>>()
        launch {
            while (isActive) {
                delay(durationMs)
                send(flattenLogItemHashMap(logItemHashMap))
            }
        }

        collect { item ->

            if (!logItemHashMap.containsKey(item.className)) {
                logItemHashMap[item.className] = DoublyLinkedList()
            }
            val itemsForViewModel = logItemHashMap[item.className]!!

            val existingItem =
                itemsForViewModel.find { it.className == item.className && it.methodName == item.methodName }
            if (existingItem != null) {
                existingItem.value.count++
                itemsForViewModel.removeNode(existingItem)
                itemsForViewModel.addLast(existingItem.value)
            } else {
                if (itemsForViewModel.getSize() > size) {
                    itemsForViewModel.removeFirst()
                }
                itemsForViewModel.addLast(LogViewItem(item.className, item.methodName, 1))
            }
        }
    }

private fun flattenLogItemHashMap(items: Map<String, DoublyLinkedList<LogViewItem>>): List<Any> {
    return items.map { it.key }.filter { items.getOrDefault(it, DoublyLinkedList()).getSize() > 0 }
        .flatMap {
            listOf(LogViewItemHeader(it)).map { it.copy() } + items.getOrDefault(
                it,
                DoublyLinkedList()
            ).toList().map { it.copy() }.sortedByDescending { it.loggedTime }.take(7)
        }.toMutableList()
}

