package com.audioburst.library.utils

import kotlin.test.Test
import kotlin.test.assertTrue

class FixedSizeQueueTest {

    @Test
    fun testIfLastlyAddedElementIsDroppedWhenAddingNewElement() {
        // GIVEN
        val queue = FixedSizeQueue<Int>(2)

        // WHEN
        queue.add(1)
        queue.add(2)
        queue.add(3)

        // THEN
        assertTrue(!queue.contains(1))
        assertTrue(queue.contains(2))
        assertTrue(queue.contains(3))
    }

    @Test
    fun testIfSizeIsCorrect() {
        // GIVEN
        val queue = FixedSizeQueue<Int>(2)

        // WHEN
        queue.add(1)
        queue.add(2)
        queue.add(3)

        // THEN
        assertTrue(queue.size == 2)
    }

    @Test
    fun testRemove() {
        // GIVEN
        val queue = FixedSizeQueue<Int>(2)

        // WHEN
        queue.add(1)
        queue.add(2)

        // THEN
        assertTrue(queue.remove() == 1)
    }

    @Test
    fun testPeekWhenQueueIsEmpty() {
        // GIVEN
        val emptyQueue = FixedSizeQueue<Int>(2)

        // WHEN
        // add nothing

        // THEN
        assertTrue(emptyQueue.peek() == null)
    }

    @Test
    fun testPeekQueueIsNotEmpty() {
        // GIVEN
        val queue = FixedSizeQueue<Int>(2)

        // WHEN
        queue.add(1)
        queue.add(2)

        // THEN
        assertTrue(queue.peek() == 1)
    }

    @Test
    fun testPollWhenQueueIsEmpty() {
        // GIVEN
        val emptyQueue = FixedSizeQueue<Int>(2)

        // WHEN
        // add nothing

        // THEN
        assertTrue(emptyQueue.peek() == null)
    }

    @Test
    fun testPollQueueIsNotEmpty() {
        // GIVEN
        val queue = FixedSizeQueue<Int>(2)

        // WHEN
        queue.add(1)
        queue.add(2)

        // THEN
        assertTrue(queue.poll() == 1)
        assertTrue(queue.size == 1)
    }
}
