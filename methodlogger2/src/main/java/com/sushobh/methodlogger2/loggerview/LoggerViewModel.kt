package com.sushobh.methodlogger2.loggerview

import LogItem
import LogViewItem
import LogViewItemHeader
import LogViewItemType
import androidx.lifecycle.ViewModel
import com.sushobh.methodlogger2.MethodLogger
import com.sushobh.methodlogger2.common.DoublyLinkedList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal class LoggerViewModel : ViewModel() {
    val groupedLogItems: Flow<List<LogViewItemType>> =
        MethodLogger.itemFlow.groupedDebounce(1000, 20)


    private fun Flow<LogItem>.groupedDebounce(durationMs: Long, size: Int = 20) =
        channelFlow {
            val logItemHashMap = HashMap<String, DoublyLinkedList<LogViewItemType.Item>>()
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
                    itemsForViewModel.find { it.logViewItem.className == item.className && it.logViewItem.methodName == item.methodName }
                if (existingItem != null) {
                    existingItem.value = existingItem.value.copy(
                        logViewItem = existingItem.value.logViewItem.copy(count = existingItem.value.logViewItem.count + 1)
                    )
                    itemsForViewModel.removeNode(existingItem)
                    itemsForViewModel.addLast(existingItem.value)
                } else {
                    if (itemsForViewModel.getSize() > size) {
                        itemsForViewModel.removeFirst()
                    }
                    itemsForViewModel.addLast(
                        LogViewItemType.Item(
                            LogViewItem(
                                item.className,
                                item.methodName,
                                1
                            )
                        )
                    )
                }
            }
        }


    private fun flattenLogItemHashMap(items: Map<String, DoublyLinkedList<LogViewItemType.Item>>): List<LogViewItemType> {
        return items.map { it.key }
            .filter { items.getOrDefault(it, DoublyLinkedList()).getSize() > 0 }
            .flatMap {
                listOf(LogViewItemType.Header(LogViewItemHeader(it))).map { it.copy() } + items.getOrDefault(
                    it,
                    DoublyLinkedList()
                ).toList().map { it.copy() }.sortedByDescending { it.logViewItem.loggedTime }
                    .take(6)
            }.toMutableList()
    }

}