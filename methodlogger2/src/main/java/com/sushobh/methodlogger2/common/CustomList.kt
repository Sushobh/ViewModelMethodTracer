package com.sushobh.methodlogger2.common

internal class DoublyLinkedList<T> {

    inner class Node<T>(var value: T) {
        var prev: Node<T>? = null
        var next: Node<T>? = null
    }

    private var head: Node<T>? = null
    private var tail: Node<T>? = null

    fun addFirst(value: T): Node<T> {
        val newNode = Node(value)
        if (head == null) {
            head = newNode
            tail = newNode
        } else {
            newNode.next = head
            head!!.prev = newNode
            head = newNode
        }
        return newNode
    }

    fun addLast(value: T): Node<T> {
        val newNode = Node(value)
        if (tail == null) {
            head = newNode
            tail = newNode
        } else {
            newNode.prev = tail
            tail!!.next = newNode
            tail = newNode
        }
        return newNode
    }

    fun removeFirst(): T? {
        val value = head?.value
        head = head?.next
        if (head != null) {
            head!!.prev = null
        } else {
            tail = null // list became empty
        }
        return value
    }

    fun removeLast(): T? {
        val value = tail?.value
        tail = tail?.prev
        if (tail != null) {
            tail!!.next = null
        } else {
            head = null // list became empty
        }
        return value
    }

    fun removeNode(node: Node<T>) {
        when {
            node.prev == null && node.next == null -> { // only one element
                head = null
                tail = null
            }

            node.prev == null -> { // node is head
                head = node.next
                head?.prev = null
            }

            node.next == null -> { // node is tail
                tail = node.prev
                tail?.next = null
            }

            else -> {
                node.prev?.next = node.next
                node.next?.prev = node.prev
            }
        }
        node.prev = null
        node.next = null
    }

    fun find(predicate: (T) -> Boolean): Node<T>? {
        var current = head
        while (current != null) {
            if (predicate(current.value)) {
                return current
            }
            current = current.next
        }
        return null
    }

    fun getSize(): Int {
        return toList().size
    }

    fun toList(): List<T> {
        val result = mutableListOf<T>()
        var current = head
        while (current != null) {
            result.add(current.value)
            current = current.next
        }
        return result
    }
}
