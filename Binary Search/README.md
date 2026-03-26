# Binary Search

A clean implementation and full analysis of the Binary Search algorithm — both iterative and recursive variants — written as a companion to the Java source code.

---

## Algorithm Overview

Binary Search finds a target value in a **sorted** array by repeatedly halving the search space. Instead of checking every element, it compares the target against the middle element and eliminates the half where the target cannot exist. This continues until the target is found or the search space is empty.

**Core idea in one sentence:** *Look at the middle — if it's not the target, eliminate the half that can't contain it, repeat.*

**Critical precondition:** The array must be sorted in non-decreasing order. Binary Search produces incorrect results on unsorted input.

---

## How It Works

Given a sorted array and a target `key`, maintain two pointers `low` and `high` defining the current search space:

1. Compute `mid = low + (high - low) / 2`
2. If `arr[mid] == key` — return `mid` (found)
3. If `arr[mid] < key` — target is in the right half: set `low = mid + 1`
4. If `arr[mid] > key` — target is in the left half: set `high = mid - 1`
5. If `low > high` — search space exhausted, return `-1` (not found)

Each iteration eliminates at least half the remaining elements.

### Why `mid = low + (high - low) / 2` and not `(low + high) / 2`

Both formulas produce the same result mathematically, but `(low + high) / 2` can cause an **integer overflow** when `low` and `high` are both large. For example, if both are near `Integer.MAX_VALUE`, their sum overflows into a negative number. The safe form `low + (high - low) / 2` avoids this entirely.

---

## Trace Examples

### Successful Search

Array: `[2, 4, 7, 10, 13, 18, 21, 25, 30]` (n=9), key = `18`

```
low=0, high=8 → mid=4 → arr[4]=13 → 13 < 18 → go right
low=5, high=8 → mid=6 → arr[6]=21 → 21 > 18 → go left
low=5, high=5 → mid=5 → arr[5]=18 → 18 = 18 → found at index 5
```

Total comparisons: `3`. Sequential Search would need `6`.

### Unsuccessful Search

Array: `[2, 4, 7, 10, 13, 18, 21, 25, 30]` (n=9), key = `15`

```
low=0, high=8 → mid=4 → arr[4]=13 → 13 < 15 → go right
low=5, high=8 → mid=6 → arr[6]=21 → 21 > 15 → go left
low=5, high=5 → mid=5 → arr[5]=18 → 18 > 15 → go left
low=5, high=4 → low > high → return -1
```

Total comparisons: `4`. Target not found.

### The Halving Pattern

```
n = 9 elements
Pass 1: search space = 9
Pass 2: search space = 4
Pass 3: search space = 2
Pass 4: search space = 1
Pass 5: search space = 0 → terminate

Maximum passes = ceil(log₂(9)) + 1 = 4
```

---

## Implementation (Java)

### Iterative

```java
public static int binarySearchIterative(int[] arr, int key) {
    int low = 0;
    int high = arr.length - 1;

    while (low <= high) {
        int mid = low + (high - low) / 2;

        if (arr[mid] == key) {
            return mid;           // found
        } else if (arr[mid] < key) {
            low = mid + 1;        // eliminate left half
        } else {
            high = mid - 1;       // eliminate right half
        }
    }

    return -1;                    // not found
}
```

### Recursive

```java
public static int binarySearchRecursive(int[] arr, int key, int low, int high) {
    if (low > high) {
        return -1;                // base case: search space empty
    }

    int mid = low + (high - low) / 2;

    if (arr[mid] == key) {
        return mid;               // found
    } else if (arr[mid] < key) {
        return binarySearchRecursive(arr, key, mid + 1, high);  // search right
    } else {
        return binarySearchRecursive(arr, key, low, mid - 1);   // search left
    }
}
```

**Initial call:**
```java
int result = binarySearchRecursive(arr, key, 0, arr.length - 1);
```

### Iterative vs Recursive — Key Differences

| Aspect | Iterative | Recursive |
|---|---|---|
| Space | O(1) | O(log n) call stack |
| Overflow risk | None | Stack overflow on very large arrays |
| Readability | Slightly more verbose | Cleaner, maps directly to definition |
| Preferred in production | Yes | Only for small inputs or academic clarity |

The iterative version is preferred in practice — it uses O(1) space and avoids any call stack depth concerns. The recursive version is cleaner to reason about formally and maps directly to the recurrence relation used in complexity proofs.

---

## Complexity Analysis

### Time Complexity

Each iteration cuts the search space in half. Starting from `n` elements, after `k` iterations the search space has size `n / 2^k`.

The algorithm terminates when the search space reaches size 1 or 0:

```
n / 2^k = 1
2^k = n
k = log₂(n)
```

Therefore at most `⌊log₂(n)⌋ + 1` iterations are needed.

| Case | Comparisons | Time |
|---|---|---|
| Best case | 1 | **O(1)** |
| Average case | ~log₂(n) | **O(log n)** |
| Worst case | ⌊log₂(n)⌋ + 1 | **O(log n)** |

**The practical impact of O(log n):**

| n | Sequential Search (worst) | Binary Search (worst) |
|---|---|---|
| 100 | 100 | 7 |
| 1,000 | 1,000 | 10 |
| 1,000,000 | 1,000,000 | 20 |
| 1,000,000,000 | 1,000,000,000 | 30 |

Binary Search needs at most 30 comparisons to find any element in an array of one billion sorted values.

### Space Complexity

| Version | Auxiliary Space | Reason |
|---|---|---|
| Iterative | **O(1)** | Only `low`, `high`, `mid` variables |
| Recursive | **O(log n)** | One stack frame per recursive call; maximum depth is log₂(n) |

### Recurrence Relation (Recursive version)

The time complexity of the recursive version is described by:

```
T(n) = T(n/2) + O(1)
T(1) = O(1)
```

By the Master Theorem (case 2 with a=1, b=2, f(n)=O(1)):

```
T(n) = O(log n)
```

---

## Properties

| Property | Value | Notes |
|---|---|---|
| Requires sorted input | **Yes** | Precondition — results are undefined on unsorted arrays |
| Returns first match | **Not guaranteed** | Returns *a* matching index, not necessarily the first |
| In-place | **Yes** (iterative) | O(1) auxiliary space |
| In-place | **No** (recursive) | O(log n) call stack |
| Time complexity | **O(log n)** | Each step halves the search space |

> **Important:** If duplicate values exist and you need the first or last occurrence, Binary Search must be modified. The standard implementation returns an arbitrary matching index.

---

## Precondition: Sorted Input

Binary Search is only correct on sorted arrays. On unsorted input it may miss existing elements or report false positives, because its core logic — "if `arr[mid] < key`, the target must be in the right half" — is only valid when elements are ordered.

If the input is not sorted:
- Sort first: O(n log n) + O(log n) = O(n log n) total — only worthwhile if multiple searches follow
- Use Sequential Search: O(n) — always correct, no preconditions

---

## Visualization

```
Array: [ 2 ][ 4 ][ 7 ][10][13][18][21][25][30]
Index:   0    1    2    3    4    5    6    7    8

key = 18

Step 1:  low=0, high=8
         mid=4 → arr[4]=13
         13 < 18 → search right half
         [  ×  ][  ×  ][  ×  ][  ×  ][  ×  ][18][21][25][30]

Step 2:  low=5, high=8
         mid=6 → arr[6]=21
         21 > 18 → search left half
         [  ×  ][  ×  ][  ×  ][  ×  ][  ×  ][18][  ×  ][  ×  ][  ×  ]

Step 3:  low=5, high=5
         mid=5 → arr[5]=18
         18 = 18 → FOUND at index 5
```

`×` marks eliminated regions. The search space shrinks visibly with each step.

---

## References

- Cormen, T. H. et al. — *Introduction to Algorithms* (CLRS), 3rd ed., §2.3 and §4.5 (Master Theorem)
- Sedgewick, R. — *Algorithms in Java*, Part 1–4, §2.1
- Knuth, D. E. — *The Art of Computer Programming*, Vol. 3: Sorting and Searching, §6.2.1
