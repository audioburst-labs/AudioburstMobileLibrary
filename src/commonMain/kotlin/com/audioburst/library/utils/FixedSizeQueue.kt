package com.audioburst.library.utils

import co.touchlab.stately.collections.IsoArrayDeque

/**
 * Copy of the interface that is available on JVM.
 */
internal interface Queue<E> : MutableCollection<E> {
    /**
     * Inserts the specified element into this queue if it is possible to do so
     * immediately without violating capacity restrictions, returning
     * `true` upon success and throwing an `IllegalStateException`
     * if no space is currently available.
     *
     * @param element the element to add
     * @return `true` (as specified by [Collection.add])
     * @throws IllegalStateException if the element cannot be added at this
     * time due to capacity restrictions
     * @throws ClassCastException if the class of the specified element
     * prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     * this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     * prevents it from being added to this queue
     */
    override fun add(element: E): Boolean

    /**
     * Inserts the specified element into this queue if it is possible to do
     * so immediately without violating capacity restrictions.
     * When using a capacity-restricted queue, this method is generally
     * preferable to [.add], which can fail to insert an element only
     * by throwing an exception.
     *
     * @param element the element to add
     * @return `true` if the element was added to this queue, else
     * `false`
     * @throws ClassCastException if the class of the specified element
     * prevents it from being added to this queue
     * @throws NullPointerException if the specified element is null and
     * this queue does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     * prevents it from being added to this queue
     */
    fun offer(element: E): Boolean

    /**
     * Retrieves and removes the head of this queue.  This method differs
     * from [poll][.poll] only in that it throws an exception if this
     * queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    fun remove(): E

    /**
     * Retrieves and removes the head of this queue,
     * or returns `null` if this queue is empty.
     *
     * @return the head of this queue, or `null` if this queue is empty
     */
    fun poll(): E?

    /**
     * Retrieves, but does not remove, the head of this queue.  This method
     * differs from [peek][.peek] only in that it throws an exception
     * if this queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    fun element(): E

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns `null` if this queue is empty.
     *
     * @return the head of this queue, or `null` if this queue is empty
     */
    fun peek(): E?
}

@OptIn(ExperimentalStdlibApi::class)
internal class FixedSizeQueue<T>(private val limit: Int) : Queue<T> {

    private val array = IsoArrayDeque<T>()

    private fun trim(): Boolean {
        val changed = array.size > limit
        while (array.size > limit) {
            array.removeFirst()
        }
        return changed
    }

    override fun contains(element: T): Boolean {
        return array.contains(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val changed = array.addAll(elements)
        val trimmed = trim()
        return changed || trimmed
    }

    override fun clear() {
        array.clear()
    }

    override fun element(): T {
        return array.first()
    }

    override fun isEmpty(): Boolean {
        return array.isEmpty()
    }

    override fun remove(): T {
        return array.removeFirst()
    }

    override val size: Int
        get() = array.size

    override fun containsAll(elements: Collection<T>): Boolean {
        return array.containsAll(elements)
    }

    override fun iterator(): MutableIterator<T> {
        return array.iterator()
    }

    override fun remove(element: T): Boolean {
        return array.remove(element)
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return array.removeAll(elements)
    }

    override fun add(element: T): Boolean {
        val changed = array.add(element)
        val trimmed = trim()
        return changed || trimmed
    }

    override fun offer(element: T): Boolean {
        val changed = array.add(element)
        val trimmed = trim()
        return changed || trimmed
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        return array.retainAll(elements)
    }

    override fun peek(): T? {
        return array.firstOrNull()
    }

    override fun poll(): T? {
        return array.removeFirstOrNull()
    }

    override fun toString(): String {
        return array.toString()
    }
}

internal fun <T> fixedQueueOf(limit: Int = 10, vararg items: T): FixedSizeQueue<T> =
    FixedSizeQueue<T>(limit).apply {
        items.forEach { item ->
            add(item)
        }
    }
