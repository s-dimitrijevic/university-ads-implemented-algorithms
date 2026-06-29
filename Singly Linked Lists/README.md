# Singly Linked List

A clean implementation and full analysis of the Singly Linked List data structure — written as a companion to the source code for the Algorithms and Data Structures course.

---

## Structure Overview

A Singly Linked List is a linear data structure where each element (node) stores data and a single pointer to the next node. The list is accessed exclusively through the head pointer. The last node points to null — this is the termination sentinel.

```
head → [data|next] → [data|next] → [data|next] → null
```

Core idea in one sentence: A chain of nodes where each node knows only its own data and who comes directly after it.

---

## Node Structure

```java
class Node {
    int data;
    Node next;

    Node(int data) {
        this.data = data;
        this.next = null;
    }
}
```

Two fields only. No backward reference. This is the defining constraint of a singly linked list — traversal is strictly one-directional: head → tail.

---

## Operations

| Operation          | Description                        | Time Complexity              |
|--------------------|------------------------------------|------------------------------|
| `insertAtHead(e)`  | Insert node at the front           | O(1)                         |
| `insertAtTail(e)`  | Insert node at the end             | O(n) / O(1) with tail pointer|
| `insertAt(i, e)`   | Insert node at index i             | O(n)                         |
| `deleteAtHead()`   | Remove first node                  | O(1)                         |
| `deleteAtTail()`   | Remove last node                   | O(n)                         |
| `deleteAt(i)`      | Remove node at index i             | O(n)                         |
| `search(e)`        | Find node by value                 | O(n)                         |
| `traverse()`       | Visit and print all nodes          | O(n)                         |
| `size()`           | Return element count               | O(1) with counter            |
| `isEmpty()`        | Check if list is empty             | O(1)                         |

---

## How It Works

### Insert at Head — O(1)

No traversal needed. New node is prepended directly.

```
Before: head → [1] → [2] → [3] → null
Insert 0 at head:

  newNode = [0]
  newNode.next = head      →  [0] → [1] → [2] → [3] → null
  head = newNode

After: head → [0] → [1] → [2] → [3] → null
```

### Insert at Tail — O(n)

Must traverse to find the last node.

```
Before: head → [1] → [2] → [3] → null
Insert 4 at tail:

  Traverse until current.next == null  →  stop at [3]
  [3].next = newNode [4]

After: head → [1] → [2] → [3] → [4] → null
```

With a `tail` pointer maintained at all times, this becomes O(1).

### Insert at Index — O(n)

Traverse to index - 1, rewire two pointers.

```
Before: head → [1] → [2] → [4] → null
Insert 3 at index 2:

  Traverse to index 1  →  stop at [2]
  newNode.next = current.next  →  [3] → [4]
  current.next = newNode       →  [2] → [3]

After: head → [1] → [2] → [3] → [4] → null
```

### Delete at Head — O(1)

```
Before: head → [1] → [2] → [3] → null

  head = head.next

After: head → [2] → [3] → null
```

### Delete at Tail — O(n)

Must traverse to the second-to-last node — there is no backward pointer.

```
Before: head → [1] → [2] → [3] → null

  Traverse until current.next.next == null  →  stop at [2]
  current.next = null

After: head → [1] → [2] → null
```

Stop condition is `current.next.next == null`, not `current.next == null`. The latter lands on the last node itself — too far; the reference to second-to-last is lost.

### Delete at Index — O(n)

```
Before: head → [1] → [2] → [3] → [4] → null
Delete at index 2 (value 3):

  Traverse to index 1  →  stop at [2]
  current.next = current.next.next  →  [2].next = [4]

After: head → [1] → [2] → [4] → null
```

---

## Implementation (Java)

```java
public class SinglyLinkedList {

    private Node head;
    private int size;

    // --- Insert ---

    public void insertAtHead(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
        size++;
    }

    public void insertAtTail(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public void insertAt(int index, int data) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Invalid index.");
        if (index == 0) { insertAtHead(data); return; }

        Node newNode = new Node(data);
        Node current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;
        size++;
    }

    // --- Delete ---

    public void deleteAtHead() {
        if (head == null) throw new IndexOutOfBoundsException("List is empty.");
        head = head.next;
        size--;
    }

    public void deleteAtTail() {
        if (head == null) throw new IndexOutOfBoundsException("List is empty.");
        if (head.next == null) {
            head = null;
            size--;
            return;
        }
        Node current = head;
        while (current.next.next != null) {
            current = current.next;
        }
        current.next = null;
        size--;
    }

    public void deleteAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index.");
        if (index == 0) { deleteAtHead(); return; }

        Node current = head;
        for (int i = 0; i < index - 1; i++) {
            current = current.next;
        }
        current.next = current.next.next;
        size--;
    }

    // --- Search ---

    public boolean search(int data) {
        Node current = head;
        while (current != null) {
            if (current.data == data) return true;
            current = current.next;
        }
        return false;
    }

    // --- Utility ---

    public int size()      { return size; }
    public boolean isEmpty() { return head == null; }

    public void traverse() {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " → ");
            current = current.next;
        }
        System.out.println("null");
    }
}
```

---

## Key Implementation Details

Three details that matter:

1. **Always check for null head** before any traversal — omitting this causes `NullPointerException` on empty list operations.
2. **Track size with a counter** — avoids O(n) traversal just to count nodes; size stays consistent if incremented/decremented on every insert/delete.
3. **Two-pointer stop condition for tail deletion** — stop at `current.next.next == null`, not `current.next == null`. The latter lands on the last node itself and loses the reference needed to null out the link.

---

## Complexity Analysis

### Time Complexity

| Operation       | Best Case | Worst Case | Notes                            |
|-----------------|-----------|------------|----------------------------------|
| `insertAtHead`  | O(1)      | O(1)       | Direct head manipulation         |
| `insertAtTail`  | O(n)      | O(n)       | O(1) only with tail pointer      |
| `insertAt(i)`   | O(1)      | O(n)       | Depends on index                 |
| `deleteAtHead`  | O(1)      | O(1)       | Direct head manipulation         |
| `deleteAtTail`  | O(n)      | O(n)       | No backward pointer              |
| `deleteAt(i)`   | O(1)      | O(n)       | Depends on index                 |
| `search`        | O(1)      | O(n)       | Linear scan from head            |
| `traverse`      | O(n)      | O(n)       | Full pass always                 |

### Space Complexity

| Space     | Complexity | Notes                            |
|-----------|------------|----------------------------------|
| Per node  | O(1)       | data + one pointer               |
| Total     | O(n)       | n nodes                          |
| Auxiliary | O(1)       | Only loop variables              |

---

## Properties

| Property           | Value | Notes                                       |
|--------------------|-------|---------------------------------------------|
| Random access      | No    | Must traverse from head each time           |
| Dynamic size       | Yes   | Grows and shrinks at runtime                |
| Memory layout      | Non-contiguous | Nodes scattered across heap          |
| Cache performance  | Poor  | No spatial locality                         |
| Traversal direction| One-way | Head → tail only                          |
| Null termination   | Yes   | Last node.next = null                       |

---

## Comparison with Array and Doubly Linked List

| Property            | Singly Linked List | Doubly Linked List | Array       |
|---------------------|--------------------|--------------------|-------------|
| Access by index     | O(n)               | O(n)               | O(1)        |
| Insert at head      | O(1)               | O(1)               | O(n)        |
| Insert at tail      | O(n) / O(1)*       | O(1)               | O(1) amort. |
| Delete at head      | O(1)               | O(1)               | O(n)        |
| Delete at tail      | O(n)               | O(1)               | O(1)        |
| Delete given node   | O(n)               | O(1)               | O(n)        |
| Memory per element  | 1 pointer          | 2 pointers         | 0 pointers  |
| Traversal direction | Forward only       | Both directions    | Both        |
| Cache efficiency    | Poor               | Poor               | Excellent   |

*O(1) tail insert requires maintaining a separate tail pointer.

---

## Exam Notes

- **No random access** — any index-based operation requires traversal from head. There is no shortcut.
- **Deletion of tail is always O(n)** for a singly linked list — there is no way to reach the second-to-last node without traversal from head.
- **A tail pointer only helps insertion** — `deleteAtTail` remains O(n) even with a tail pointer, because the predecessor of tail is still unreachable without traversal.
- **Always draw pointer changes before coding deletion** — reassigning pointers in the wrong order severs the list and loses nodes permanently.
- **The null check at the start of every method is not optional** — an empty list is a valid state; all operations must handle it explicitly.
