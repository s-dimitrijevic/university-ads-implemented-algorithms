# Queue

A clean implementation and full analysis of the Queue data structure — written as a companion to the source code for the Algorithms and Data Structures course.

---

## Structure Overview

A Queue is a linear data structure that enforces First In, First Out (FIFO) ordering. Elements are inserted at the rear and removed from the front. The two ends are always maintained as separate pointers.

```
enqueue →  [front] → [ ] → [ ] → [rear]  → dequeue
```

Core idea in one sentence: The first element inserted is always the first element removed — like a real queue at a bank, where you join at the back and leave from the front.

---

## Operations

| Operation    | Description                              | Time Complexity |
|--------------|------------------------------------------|-----------------|
| `enqueue(e)` | Add element to the rear                  | O(1)            |
| `dequeue()`  | Remove element from the front            | O(1)            |
| `peek()`     | Return front element without removing    | O(1)            |
| `peekEnd()`  | Return rear element without removing     | O(1)            |
| `isEmpty()`  | Check if queue is empty                  | O(1)            |
| `size()`     | Return number of elements                | O(1)            |

All standard operations are O(1). Any implementation that traverses on enqueue or dequeue is architecturally incorrect.

---

## FIFO — How It Works

### Enqueue sequence: 1, 2, 3

```
enqueue(1):  front → [1] ← rear
enqueue(2):  front → [1] → [2] ← rear
enqueue(3):  front → [1] → [2] → [3] ← rear
```

### Dequeue sequence from above state

```
dequeue() → 1:  front → [2] → [3] ← rear
dequeue() → 2:  front → [3] ← rear
dequeue() → 3:  front → null  (empty)
```

The element that waited longest is always served first.

---

## Trace Example

```
Operation       Queue State (front → rear)    Returns
---------       --------------------------    -------
enqueue(5)      [5]                           —
enqueue(3)      [5, 3]                        —
enqueue(8)      [5, 3, 8]                     —
peek()          [5, 3, 8]                     5
dequeue()       [3, 8]                        —
enqueue(1)      [3, 8, 1]                     —
dequeue()       [8, 1]                        —
peek()          [8, 1]                        8
peekEnd()       [8, 1]                        1
dequeue()       [1]                           —
dequeue()       []                            —
isEmpty()       []                            true
dequeue()       []                            throws exception
```

---

## Why Two Pointers

A single pointer forces either O(n) enqueue or O(n) dequeue. Maintaining both `front` and `rear` pointers keeps both operations O(1) at the cost of one additional reference.

```
front → [A] → [B] → [C] ← rear

dequeue: front = front.next          (no traversal — O(1))
enqueue: rear.next = newNode         (no traversal — O(1))
         rear = newNode
```

The moment one pointer is dropped, traversal becomes unavoidable.

---

## Implementation (Java) — Linked List Based

```java
public class QueueList {

    private Node front;
    private Node rear;
    private int size;

    public void enqueue(int data) {
        Node newNode = new Node(data);
        if (size == 0) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;  // current rear links to new node
            rear = newNode;       // new node becomes the rear
        }
        size++;
    }

    public void dequeue() {
        if (size == 0) throw new IndexOutOfBoundsException("Queue is empty.");
        if (size == 1) {
            front = null;
            rear = null;
        } else {
            front = front.next;   // advance front pointer — O(1)
        }
        size--;
    }

    public int peek() {
        if (size == 0) throw new IndexOutOfBoundsException("Queue is empty.");
        return front.data;
    }

    public int peekEnd() {
        if (size == 0) throw new IndexOutOfBoundsException("Queue is empty.");
        return rear.data;
    }

    public boolean isEmpty() { return size == 0; }
    public int size()        { return size; }
}
```

### Pointer Mechanics — Enqueue

```
Before: front → [A] → [B] ← rear

enqueue(C):
  rear.next = [C]    →   [B] → [C]
  rear = newNode     →   rear points to [C]

After: front → [A] → [B] → [C] ← rear
```

### Pointer Mechanics — Dequeue

```
Before: front → [A] → [B] → [C] ← rear

dequeue():
  front = front.next   →   front points to [B]

After: front → [B] → [C] ← rear
       [A] is now unreferenced — garbage collected
```

No explicit unlinking needed. Once `front` advances, the old node has no references and is collected by the JVM.

---

## Key Implementation Details

Three details that matter:

1. **Single-node edge case on dequeue** — when `size == 1`, both `front` and `rear` point to the same node. After removal, both must be set to null. Setting only `front = front.next` leaves `rear` pointing to a garbage-collected node — a dangling reference.
2. **`size()` must never throw** — it is a neutral query operation. Returning 0 on empty is correct. Throwing on empty `size()` is a design error.
3. **Linked list direction is front → rear** — new nodes are appended at the rear via `rear.next`. This aligns with the standard left-to-right convention and makes the pointer mechanics predictable.

---

## Complexity Analysis

### Time Complexity

| Operation    | Time | Notes                                     |
|--------------|------|-------------------------------------------|
| `enqueue`    | O(1) | `rear` pointer gives direct rear access   |
| `dequeue`    | O(1) | `front` pointer gives direct front access |
| `peek`       | O(1) | Read `front.data` directly                |
| `peekEnd`    | O(1) | Read `rear.data` directly                 |
| `isEmpty`    | O(1) | Check size or null                        |
| `size`       | O(1) | Maintained as counter                     |

### Space Complexity

| Space     | Complexity | Notes                          |
|-----------|------------|--------------------------------|
| Per node  | O(1)       | data + one next pointer        |
| Total     | O(n)       | n nodes                        |
| Auxiliary | O(1)       | Only pointer variables         |

---

## Properties

| Property           | Value   | Notes                                            |
|--------------------|---------|--------------------------------------------------|
| Ordering           | FIFO    | First inserted is first removed                  |
| Access restriction | Both ends only | No middle access                          |
| Dynamic size       | Yes     | Linked list grows as needed                      |
| Fixed size variant | Yes     | `ArrayBlockingQueue` in Java — bounded capacity  |
| `isFull()`         | N/A     | Only relevant in fixed-size implementations      |

---

## Real-World Applications

| Domain                 | Use                                                    |
|------------------------|--------------------------------------------------------|
| OS scheduling          | Process queues — CPU time allocation                   |
| Network buffers        | Packet queues — FIFO packet processing                 |
| Print spooling         | Jobs processed in arrival order                        |
| BFS traversal          | Nodes enqueued level by level                          |
| Producer-consumer      | Thread-safe buffer between producer and consumer       |
| Keyboard input buffer  | Keystrokes queued and processed in order               |

---

## Comparison with Stack

| Property           | Queue (FIFO)        | Stack (LIFO)        |
|--------------------|---------------------|---------------------|
| Insert end         | Rear                | Top                 |
| Remove end         | Front               | Top                 |
| Access             | Front only          | Top only            |
| Order preserved    | Insertion order     | Reverse insertion   |
| Pointers needed    | Two (front, rear)   | One (top)           |
| Primary use        | Scheduling, BFS     | Recursion, DFS      |

---

## Exam Notes

- **FIFO is the only defining rule** — any implementation that violates insertion-order removal is not a queue.
- **Both enqueue and dequeue must be O(1)** — the moment either operation requires traversal, the two-pointer design has been violated.
- **rear → front pointer direction is a common trap** — prepending new nodes instead of appending creates an intuitive structure (new node points toward front, like a real queue) but forces O(n) dequeue since the predecessor of front becomes unreachable. The efficient direction is front → rear.
- **BFS uses a queue for the same reason scheduling does** — nodes at the current level are processed before nodes at the next level; FIFO enforces this naturally.
- **Java's `ArrayDeque` is the standard queue implementation** — it is backed by a circular array and provides O(1) at both ends with better cache performance than a linked list.
