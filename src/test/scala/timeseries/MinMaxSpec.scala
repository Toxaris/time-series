// Copyright 2016 Tillmann Rendel.
// All Rights Reserved.

package timeseries

import org.scalatest._

class MinMaxSpec extends FlatSpec with Matchers {
  "MinMax.Stack" should "support last-in first-out semantics" in {
    val stack = MinMax.Stack()

    stack.push(1, 5.0)
    stack.topKey should be (1)
    stack.topValue should be (5.0)

    stack.push(2, 6.0)
    stack.topKey should be (2)
    stack.topValue should be (6.0)

    stack.push(3, 7.0)
    stack.topKey should be (3)
    stack.topValue should be (7.0)

    stack.pop()
    stack.topKey should be (2)
    stack.topValue should be (6.0)

    stack.pop()
    stack.topKey should be (1)
    stack.topValue should be (5.0)
  }

  it should "keep track of the minimum value" in {
    val stack = MinMax.Stack()

    stack.push(1, 3.0)
    stack.min should be (3.0)

    stack.push(2, 2.0)
    stack.min should be (2.0)

    stack.push(3, 1.0)
    stack.min should be (1.0)

    stack.pop()
    stack.min should be (2.0)

    stack.pop()
    stack.min should be (3.0)
  }

  it should "keep track of the maximum value" in {
    val stack = MinMax.Stack()

    stack.push(1, 1.0)
    stack.max should be (1.0)

    stack.push(2, 2.0)
    stack.max should be (2.0)

    stack.push(3, 3.0)
    stack.max should be (3.0)

    stack.pop()
    stack.max should be (2.0)

    stack.pop()
    stack.max should be (1.0)
  }

  "MinMax.Queue" should "support first-in first-out semantics" in {
    val queue = MinMax.Queue()

    queue.enqueue(1, 5.0)
    queue.nextKey should be (1)
    queue.nextValue should be (5.0)

    queue.enqueue(2, 6.0)
    queue.nextKey should be (1)
    queue.nextValue should be (5.0)

    queue.enqueue(3, 7.0)
    queue.nextKey should be (1)
    queue.nextValue should be (5.0)

    queue.dequeue()
    queue.nextKey should be (2)
    queue.nextValue should be (6.0)

    queue.dequeue()
    queue.nextKey should be (3)
    queue.nextValue should be (7.0)
}

  it should "keep track of the minimum value" in {
    val queue = MinMax.Queue()

    queue.enqueue(1, 1.0)
    queue.min should be (1.0)

    queue.enqueue(2, 3.0)
    queue.min should be (1.0)

    queue.dequeue()
    queue.min should be (3.0)

    queue.enqueue(3, 2.0)
    queue.min should be (2.0)

    queue.dequeue()
    queue.min should be (2.0)
  }

  it should "keep track of the maximum value" in {
    val queue = MinMax.Queue()

    queue.enqueue(1, 3.0)
    queue.max should be (3.0)

    queue.enqueue(2, 1.0)
    queue.max should be (3.0)

    queue.dequeue()
    queue.max should be (1.0)

    queue.enqueue(3, 2.0)
    queue.max should be (2.0)

    queue.dequeue()
    queue.max should be (2.0)
  }
}
