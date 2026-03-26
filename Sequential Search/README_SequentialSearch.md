# Sequential Search

A clean implementation and full analysis of the Sequential Search algorithm — written as a companion to the Java source code.

---

## Algorithm Overview

Sequential Search (also called Linear Search) scans an array from left to right, comparing each element against the target value. If a match is found, the index is returned. If the entire array is exhausted without a match, `-1` is returned to signal failure.

**Core idea in one sentence:** *Walk through every element one by one until you find the target or run out of elements.*

No assumptions are made about the order of elements — the array can be completely unsorted.

---

## How It Works

Given an array of `n` elements and a target value `key`:

1. Start at index `0`
2. Compare `arr[i]` with `key`
3. If equal — return `i` (found)
4. If not equal — move to index `i + 1`
5. If `i` reaches `n` without a match — return `-1` (not found)

### Trace Example — Successful Search

Array: `[7, 2, 15, 4, 9, 11, 3]`, key = `9`

```
i=0: arr[0]=7  → 7 == 9?  No
i=1: arr[1]=2  → 2 == 9?  No
i=2: arr[2]=15 → 15 == 9? No
i=3: arr[3]=4  → 4 == 9?  No
i=4: arr[4]=9  → 9 == 9?  Yes → return 4
```

Result: found at index `4`. Total comparisons: `5`.

### Trace Example — Unsuccessful Search

Array: `[7, 2, 15, 4, 9, 11, 3]`, key = `6`

```
i=0: arr[0]=7  → 7 == 6?  No
i=1: arr[1]=2  → 2 == 6?  No
i=2: arr[2]=15 → 15 == 6? No
i=3: arr[3]=4  → 4 == 6?  No
i=4: arr[4]=9  → 9 == 6?  No
i=5: arr[5]=11 → 11 == 6? No
i=6: arr[6]=3  → 3 == 6?  No
End of array → return -1
```

Result: not found. Total comparisons: `7` (the full array length).

---

## Implementation (Java)

```java
public static int sequentialSearch(int[] arr, int key) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == key) {
            return i;   // target found — return its index
        }
    }
    return -1;          // target not found
}
```

**Simple. No preconditions. No auxiliary structures.**

The algorithm makes exactly one comparison per element visited. It returns immediately on the first match — it does not continue scanning after finding the target.

---

## Complexity Analysis

### Time Complexity

The number of comparisons depends entirely on where the target is located (or whether it exists at all).

**Best case:** The target is at index `0` — found on the very first comparison.

**Worst case:** The target is at the last index, or is not in the array at all — every element is compared.

**Average case:** Assuming the target is present and equally likely to be at any position, the expected number of comparisons is:

```
(1 + 2 + 3 + ... + n) / n = (n + 1) / 2
```

This is approximately `n/2` — still linear.

| Case | Comparisons | Time |
|---|---|---|
| Best case | 1 | **O(1)** |
| Average case | (n + 1) / 2 | **O(n)** |
| Worst case | n | **O(n)** |

> The algorithm is **Θ(n)** in the average and worst case. The O(1) best case is a degenerate edge — never assume it.

### Space Complexity

Sequential Search uses no auxiliary data structures — only a loop index variable.

| Space | Complexity |
|---|---|
| Auxiliary space | **O(1)** |
| Total space | **O(n)** (the input array) |

---

## Properties

| Property | Value | Notes |
|---|---|---|
| Requires sorted input | **No** | Works on any array regardless of order |
| Returns first match | **Yes** | Stops at the first occurrence found |
| In-place | **Yes** | O(1) auxiliary space |
| Stable | **N/A** | Search algorithm — does not reorder elements |
| Time complexity | **O(n)** | Linear scan, no shortcuts |

---

## When Sequential Search Is Appropriate

Despite being the slowest general-purpose search, Sequential Search has legitimate use cases:

1. **Unsorted data** — if the array is not sorted, Binary Search cannot be used. Sequential Search is the only option without preprocessing.
2. **Small arrays** — for `n ≤ ~20`, the overhead of sorting + Binary Search exceeds the cost of a simple linear scan.
3. **Single search on unsorted data** — sorting costs O(n log n). If you only need to search once, a linear scan at O(n) is cheaper than sorting first.
4. **Linked lists** — Binary Search requires random access (O(1) index lookup). Linked lists only support sequential access, making Sequential Search the natural choice.
5. **Searching by complex condition** — when the match condition is not a simple equality comparison (e.g., first element satisfying a predicate), a linear scan is the standard approach.

---

## Visualization

```
Array:  [ 7 ][ 2 ][ 15][ 4 ][ 9 ][ 11][ 3 ]
Index:    0    1    2    3    4    5    6

key = 9

Step 1:  [>7 ][ 2 ][ 15][ 4 ][ 9 ][ 11][ 3 ]   7  ≠ 9
Step 2:  [ 7 ][>2 ][ 15][ 4 ][ 9 ][ 11][ 3 ]   2  ≠ 9
Step 3:  [ 7 ][ 2 ][>15][ 4 ][ 9 ][ 11][ 3 ]   15 ≠ 9
Step 4:  [ 7 ][ 2 ][ 15][>4 ][ 9 ][ 11][ 3 ]   4  ≠ 9
Step 5:  [ 7 ][ 2 ][ 15][ 4 ][>9 ][ 11][ 3 ]   9  = 9  → found at index 4
```

The `>` marker shows the current position of the scan.

---

## References

- Cormen, T. H. et al. — *Introduction to Algorithms* (CLRS), 3rd ed., §2.1 (Exercise 2.1-3)
- Sedgewick, R. — *Algorithms in Java*, Part 1–4, §2.1
- Knuth, D. E. — *The Art of Computer Programming*, Vol. 3: Sorting and Searching, §6.1
