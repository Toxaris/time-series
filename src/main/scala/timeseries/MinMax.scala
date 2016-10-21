// Copyright 2016 Tillmann Rendel.
// All Rights Reserved.

package timeseries

/** Data structures that incrementally maintain minimum and maximum. */
object MinMax {
  type Key = Long
  type Value = Double

  /** Information we remember for every datapoint. */
  private case class Info(key: Key, value: Value, min: Value, max: Value)

  object Stack {
    /** Create empty MinMax.Stack. */
    def apply() =
      new Stack
  }

  /** Mutable stack of key-value pairs.
    *
    * Incrementally maintains the minimum and maximum value.
    *
    * All operations are O(1) in the stack size.
    */
  // TODO: Unpack ArrayStack[Info] into four stacks to avoid Info allocation?
  class Stack {
    /** Elements. */
    private val elements = collection.mutable.ArrayStack[Info]()

    /** Minimum ratio. */
    var min = Double.PositiveInfinity

    /** Maximum ratio. */
    var max = Double.NegativeInfinity

    /** Is this stack empty? */
    def isEmpty: Boolean =
      elements.isEmpty

    /** Add an element to the stack. */
    def push(key: Key, value: Value) {
      elements.push(Info(key, value, min, max))
      min = Math.min(min, value)
      max = Math.max(max, value)
    }

    /** Pop from the stack. */
    def pop() {
      val element = elements.pop
      min = element.min
      max = element.max
    }

    /** Return key at top of stack without removing it. */
    def topKey: Key =
      elements.top.key

    /** Return value at top of stack without removing it. */
    def topValue: Value =
      elements.top.value
  }

  object Queue {
    /** Create empty MinMax.Queue. */
    def apply() =
      new Queue
  }

  /** Mutable queue of key-value pairs.
    *
    * Incrementally maintains the minimum and maximum value.
    *
    * All operations except for `dequeue` run in O(1) time in the
    * queue size. The `dequeue` operation runs in amortized O(1)
    * time but can take up to O(n) time in the worst-case.
    */
  class Queue {
    /** Front part of the queue, where data points are enqueued. */
    val front = Stack()

    /** Back part of the queue, where data points are dequeued. */
    val back = Stack()

    /** Is this queue empty? */
    def isEmpty: Boolean =
      front.isEmpty && back.isEmpty

    /** Add a datapoint to the back of the queue. */
    def enqueue(key: Key, value: Value) {
      back.push(key, value)
    }

    /** Rotate the back onto the front, if the front is empty. */
    private def rotateIfNecessary() {
      if (front.isEmpty) {
        // Amortized O(1) because every datapoint in the queue is
        // rotated over at most once
        while (!back.isEmpty) {
          front.push(back.topKey, back.topValue)
          back.pop()
        }
      }
    }

    /** Remove a datapoint from the front of the queue. */
    def dequeue() {
      rotateIfNecessary()
      front.pop()
    }

    /** Key that would be dequeued next. */
    def nextKey: Key = {
      rotateIfNecessary()
      front.topKey
    }

    /** Value that would be dequeued next. */
    def nextValue: Value = {
      rotateIfNecessary()
      front.topValue
    }

    /** Minimum value in queue. */
    def min: Value =
      Math.min(front.min, back.min)

    /** Maximum value in queue. */
    def max: Value =
      Math.max(front.max, back.max)
  }
}
