# Stack

A clean implementation and full analysis of the Stack data structure — written as a companion to the source code for the Algorithms and Data Structures course.

---

## Structure Overview

A Stack is a linear data structure that enforces Last In, First Out (LIFO) ordering. Elements are inserted and removed from the same end — the top. There is no access to any element except the one currently at the top.

```
push → | [top]  | ← pop / peek
       | [    ] |
       | [    ] |
       | [bot] |
       +--------+
```

Core idea in one sentence: The last element pushed is always the first element popped — access is restricted exclusively to the top.

---

## Operations

| Operation   | Description                              | Time Complexity |
|-------------|------------------------------------------|-----------------|
| `push(e)`   | Add element to the top                   | O(1)            |
| `pop()`     | Remove and return top element            | O(1)            |
| `peek()`    | Return top element without removing      | O(1)            |
| `isEmpty()` | Check if stack is empty                  | O(1)            |
| `size()`    | Return number of elements                | O(1)            |

All operations are O(1). A stack that requires traversal is a broken implementation.

---

## LIFO — How It Works

### Push sequence: 1, 2, 3

```
push(1):       push(2):       push(3):
| [1] | ← top | [2] | ← top  | [3] | ← top
|     |        | [1] |        | [2] |
|     |        |     |        | [1] |
```

### Pop sequence from above state

```
pop() → 3:     pop() → 2:    pop() → 1:
| [2] | ← top | [1] | ← top  |     | ← top (empty)
| [1] |        |     |        |     |
```

The structure is fully symmetric: whatever was pushed last, comes out first.

---

## Trace Example

```
Operation     Stack State         Returns
---------     -----------         -------
push(5)       [5]                 —
push(3)       [5, 3]              —
push(8)       [5, 3, 8]          —
peek()        [5, 3, 8]          8
pop()         [5, 3]             8
push(1)       [5, 3, 1]          —
pop()         [5, 3]             1
pop()         [5]                3
isEmpty()     [5]                false
pop()         []                 5
isEmpty()     []                 true
pop()         []                 throws exception
```

---

## Implementation (Java) — Linked List Based

A linked list-backed stack has no fixed capacity. The top of the stack maps directly to the head of the list.

```java
public class Stack {

    private Node top;
    private int size;

    public void push(int data) {
        Node newNode = new Node(data);
        newNode.next = top;   // new node points to current top
        top = newNode;        // new node becomes the top
        size++;
    }

    public int pop() {
        if (isEmpty()) throw new IndexOutOfBoundsException("Stack is empty.");
        int data = top.data;
        top = top.next;       // advance top to next node
        size--;
        return data;
    }

    public int peek() {
        if (isEmpty()) throw new IndexOutOfBoundsException("Stack is empty.");
        return top.data;
    }

    public boolean isEmpty() { return top == null; }
    public int size()        { return size; }
}
```

### Pointer Mechanics

Push — prepend to the list:
```
Before: top → [3] → [2] → [1] → null
push(5):
  newNode.next = top   →  [5] → [3]
  top = newNode        →  top points to [5]
After:  top → [5] → [3] → [2] → [1] → null
```

Pop — remove head of the list:
```
Before: top → [5] → [3] → [2] → [1] → null
pop():
  data = top.data      →  5
  top = top.next       →  top points to [3]
After:  top → [3] → [2] → [1] → null
Returns: 5
```

---

## Implementation (Java) — Array Based

Fixed-capacity implementation. Useful when maximum size is known upfront. `isFull()` becomes meaningful here.

```java
public class StackArray {

    private int[] data;
    private int top;
    private int capacity;

    public StackArray(int capacity) {
        this.capacity = capacity;
        this.data = new int[capacity];
        this.top = -1;   // -1 means empty
    }

    public void push(int value) {
        if (isFull()) throw new IndexOutOfBoundsException("Stack is full.");
        data[++top] = value;
    }

    public int pop() {
        if (isEmpty()) throw new IndexOutOfBoundsException("Stack is empty.");
        return data[top--];
    }

    public int peek() {
        if (isEmpty()) throw new IndexOutOfBoundsException("Stack is empty.");
        return data[top];
    }

    public boolean isEmpty() { return top == -1; }
    public boolean isFull()  { return top == capacity - 1; }
    public int size()        { return top + 1; }
}
```

The `top` index starts at -1 and increments before writing (`data[++top]`). On pop, it reads then decrements (`data[top--]`). The data is not erased on pop — `top` index moving back is sufficient; any future push overwrites the slot.

---

## Key Implementation Details

Three details that matter:

1. **Linked list top maps to head, not tail** — push and pop must both operate on the same end in O(1). The head of a linked list is the only end that allows O(1) insertion and removal.
2. **Array-based top starts at -1, not 0** — index 0 is a valid occupied slot. The sentinel for empty must be -1; using 0 causes off-by-one errors in both `isEmpty()` and `isFull()`.
3. **Always throw on empty pop/peek** — returning a default value like -1 silently hides bugs. Throw `IndexOutOfBoundsException` or a custom `StackUnderflowException` to surface the error immediately.

---

## Complexity Analysis

### Time Complexity

| Operation   | Linked List | Array  | Notes                        |
|-------------|-------------|--------|------------------------------|
| `push`      | O(1)        | O(1)   | No traversal, direct top     |
| `pop`       | O(1)        | O(1)   | No traversal, direct top     |
| `peek`      | O(1)        | O(1)   | Read only, direct top        |
| `isEmpty`   | O(1)        | O(1)   | Check null or index          |
| `size`      | O(1)        | O(1)   | Maintained as counter        |

### Space Complexity

| Implementation | Auxiliary Space | Notes                             |
|----------------|-----------------|-----------------------------------|
| Linked list    | O(n)            | One node per element              |
| Array          | O(capacity)     | Allocated upfront, fixed          |

---

## Properties

| Property        | Linked List Stack | Array Stack     |
|-----------------|-------------------|-----------------|
| Capacity        | Unbounded         | Fixed           |
| Memory overhead | High (pointer)    | Low             |
| Cache behavior  | Poor              | Excellent       |
| `isFull()`      | Not applicable    | Required        |
| Overflow risk   | No (heap bound)   | Yes             |

---

## Real-World Applications

| Domain                 | Use                                                   |
|------------------------|-------------------------------------------------------|
| Program execution      | Call stack — function frames pushed/popped            |
| Expression evaluation  | Operator/operand stacks in compilers                  |
| Undo/Redo              | Each action pushed; undo pops                        |
| Syntax parsing         | Bracket/parenthesis matching                          |
| DFS traversal          | Iterative depth-first search                          |
| Backtracking           | State saved on stack, restored on backtrack           |

---

## Comparison with Queue

| Property          | Stack (LIFO)       | Queue (FIFO)       |
|-------------------|--------------------|--------------------|
| Insert end        | Top                | Rear               |
| Remove end        | Top                | Front              |
| Access            | Top only           | Front only         |
| Order preserved   | Reverse insertion  | Insertion order    |
| Primary use       | Recursion, DFS     | Scheduling, BFS    |

---

## Exam Notes

- **LIFO is the only defining rule** — any implementation that violates LIFO ordering is not a stack.
- **All five operations must be O(1)** — a stack that traverses on push or pop is architecturally wrong.
- **The call stack is a hardware stack** — recursion is implemented by the CPU pushing return addresses and local variables. Stack overflow occurs when recursive depth exceeds the allocated call stack size.
- **Iterative DFS uses an explicit stack** — it replicates exactly what the recursive call stack does implicitly.
- **Bracket matching algorithm** — push every opening bracket; on closing bracket, pop and check for match. If stack is empty at end, input is balanced.
