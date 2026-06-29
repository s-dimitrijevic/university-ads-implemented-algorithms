# Doubly Linked List

A clean implementation and full analysis of the Doubly Linked List data structure — written as a companion to the source code for the Algorithms and Data Structures course.

---

## Structure Overview

A Doubly Linked List extends the singly linked list by giving each node a second pointer — one to the next node and one to the previous node. The list is accessed through both a head and a tail pointer. Both ends are null-terminated.

```
null ← [prev|data|next] ↔ [prev|data|next] ↔ [prev|data|next] → null
         head                                    tail
```

Core idea in one sentence: A chain of nodes where each node knows both who comes before it and who comes after it — enabling O(1) operations at both ends and O(1) deletion given any node reference.

---

## Node Structure

```java
class Node {
    int data;
    Node next;
    Node prev;

    Node(int data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
```

The `prev` pointer is the structural difference from a singly linked list. It is the reason doubly linked lists can delete at the tail in O(1) and delete any given node in O(1).

---

## Operations

| Operation             | Description                        | Time Complexity |
|-----------------------|------------------------------------|-----------------|
| `insertAtHead(e)`     | Insert node at the front           | O(1)            |
| `insertAtTail(e)`     | Insert node at the end             | O(1)            |
| `insertAt(i, e)`      | Insert node at index i             | O(n)            |
| `deleteAtHead()`      | Remove first node                  | O(1)            |
| `deleteAtTail()`      | Remove last node                   | O(1)            |
| `deleteAt(i)`         | Remove node at index i             | O(n)            |
| `deleteNode(node)`    | Remove a given node directly       | O(1)            |
| `search(e)`           | Find node by value                 | O(n)            |
| `traverseForward()`   | Visit all nodes head → tail        | O(n)            |
| `traverseBackward()`  | Visit all nodes tail → head        | O(n)            |
| `size()`              | Return element count               | O(1)            |
| `isEmpty()`           | Check if list is empty             | O(1)            |

---

## How It Works

### Insert at Head — O(1)

```
Before: head → [1] ↔ [2] ↔ [3] ← tail

Insert 0 at head:
  newNode = [0]
  newNode.next = head        →  [0] → [1]
  head.prev = newNode        →  [0] ← [1]
  head = newNode

After: head → [0] ↔ [1] ↔ [2] ↔ [3] ← tail
```

### Insert at Tail — O(1)

```
Before: head → [1] ↔ [2] ↔ [3] ← tail

Insert 4 at tail:
  newNode = [4]
  tail.next = newNode        →  [3] → [4]
  newNode.prev = tail        →  [3] ← [4]
  tail = newNode

After: head → [1] ↔ [2] ↔ [3] ↔ [4] ← tail
```

### Delete at Head — O(1)

```
Before: head → [1] ↔ [2] ↔ [3] ← tail

  head = head.next
  head.prev = null

After: head → [2] ↔ [3] ← tail
```

### Delete at Tail — O(1)

This is the critical advantage over singly linked lists. The `prev` pointer gives direct access to the second-to-last node — no traversal.

```
Before: head → [1] ↔ [2] ↔ [3] ← tail

  tail = tail.prev
  tail.next = null

After: head → [1] ↔ [2] ← tail
```

### Delete Given Node Reference — O(1)

Given a direct reference to any node, it can be removed without traversal.

```
Before: head → [1] ↔ [2] ↔ [3] ↔ [4] ← tail
Delete node [3] directly:

  [3].prev.next = [3].next    →  [2].next = [4]
  [3].next.prev = [3].prev    →  [4].prev = [2]

After: head → [1] ↔ [2] ↔ [4] ← tail
```

Must handle edge cases: if node is head, delegate to `deleteAtHead()`; if node is tail, delegate to `deleteAtTail()`.

---

## Implementation (Java)

```java
public class DoublyLinkedList {

    private Node head;
    private Node tail;
    private int size;

    // --- Insert ---

    public void insertAtHead(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public void insertAtTail(int data) {
        Node newNode = new Node(data);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public void insertAt(int index, int data) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException("Invalid index.");
        if (index == 0)    { insertAtHead(data); return; }
        if (index == size) { insertAtTail(data); return; }

        Node newNode = new Node(data);
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        // current is now the node currently at index — insert before it
        Node previous = current.prev;
        previous.next = newNode;
        newNode.prev  = previous;
        newNode.next  = current;
        current.prev  = newNode;
        size++;
    }

    // --- Delete ---

    public void deleteAtHead() {
        if (head == null) throw new IndexOutOfBoundsException("List is empty.");
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
    }

    public void deleteAtTail() {
        if (tail == null) throw new IndexOutOfBoundsException("List is empty.");
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
    }

    public void deleteNode(Node node) {
        if (node == null) throw new IllegalArgumentException("Node is null.");
        if (node == head) { deleteAtHead(); return; }
        if (node == tail) { deleteAtTail(); return; }

        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    public void deleteAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Invalid index.");
        if (index == 0)        { deleteAtHead(); return; }
        if (index == size - 1) { deleteAtTail(); return; }

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        deleteNode(current);
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

    public int size()        { return size; }
    public boolean isEmpty() { return head == null; }

    public void traverseForward() {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " → ");
            current = current.next;
        }
        System.out.println("null");
    }

    public void traverseBackward() {
        Node current = tail;
        while (current != null) {
            System.out.print(current.data + " → ");
            current = current.prev;
        }
        System.out.println("null");
    }
}
```

---

## Key Implementation Details

Four details that matter:

1. **Maintain both `head` and `tail` pointers** — without `tail`, `insertAtTail` and `deleteAtTail` degrade to O(n), eliminating the primary advantage over singly linked lists.
2. **Single-node edge case** — when `head == tail`, deletion must null both pointers, not just one. Failing to do so leaves a dangling reference.
3. **Order of pointer reassignment in insertion** — wire the new node's pointers before rewiring the existing nodes'. Doing it in reverse order can sever the list.
4. **`deleteNode()` delegates to `deleteAtHead/Tail`** — do not duplicate the single-node edge case logic. Delegate and keep the code DRY.

---

## Complexity Analysis

### Time Complexity

| Operation           | Time | Notes                                     |
|---------------------|------|-------------------------------------------|
| `insertAtHead`      | O(1) | Direct head manipulation                  |
| `insertAtTail`      | O(1) | Direct tail manipulation                  |
| `insertAt(i)`       | O(n) | Traversal to index required               |
| `deleteAtHead`      | O(1) | Direct head manipulation                  |
| `deleteAtTail`      | O(1) | `tail.prev` gives immediate predecessor   |
| `deleteNode(node)`  | O(1) | Given reference — no traversal            |
| `deleteAt(i)`       | O(n) | Traversal to index required               |
| `search`            | O(n) | Linear scan                               |
| `traverse`          | O(n) | Full pass                                 |

### Space Complexity

| Space     | Complexity | Notes                              |
|-----------|------------|------------------------------------|
| Per node  | O(1)       | data + two pointers                |
| Total     | O(n)       | n nodes                            |
| Auxiliary | O(1)       | Only loop variables                |

---

## Properties

| Property              | Value     | Notes                                         |
|-----------------------|-----------|-----------------------------------------------|
| Random access         | No        | Must traverse from head or tail               |
| Dynamic size          | Yes       | Grows and shrinks at runtime                  |
| Memory layout         | Non-contiguous | Nodes scattered across heap              |
| Traversal direction   | Both      | Forward (next) and backward (prev)            |
| Cache performance     | Poor      | No spatial locality                           |
| O(1) tail operations  | Yes       | Both insert and delete at tail                |
| O(1) node deletion    | Yes       | Given a direct node reference                 |

---

## Comparison with Singly Linked List

| Property              | Singly Linked List | Doubly Linked List |
|-----------------------|--------------------|--------------------|
| Insert at head        | O(1)               | O(1)               |
| Insert at tail        | O(n) / O(1)*       | O(1)               |
| Delete at head        | O(1)               | O(1)               |
| Delete at tail        | O(n)               | O(1)               |
| Delete given node     | O(n)               | O(1)               |
| Traversal direction   | Forward only       | Both directions    |
| Memory per node       | 1 pointer          | 2 pointers         |
| Implementation        | Simpler            | More complex       |

*O(1) tail insert in singly linked list requires a tail pointer, but tail delete remains O(n) regardless.

---

## Exam Notes

- **The `prev` pointer is the entire point** — it enables O(1) tail deletion and O(1) node deletion. If you cannot articulate this, you do not understand the structure.
- **`deleteNode(node)` is O(1) only if you already have the reference** — obtaining the reference by searching is still O(n). The O(1) applies to the deletion step itself.
- **Draw the pointer state before and after every operation** — with four pointers changing per insertion (newNode.next, newNode.prev, neighbor.next, neighbor.prev), it is easy to miss one.
- **Single-node state is the most dangerous edge case** — head and tail point to the same node. Deletion must null both; failure leaves a dangling pointer that causes subtle bugs.
- **Java deques (`ArrayDeque`, `LinkedList`) are backed by doubly linked list logic** — understanding this structure explains why they offer O(1) at both ends.
