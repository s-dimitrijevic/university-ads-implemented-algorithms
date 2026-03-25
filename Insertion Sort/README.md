# Insertion Sort

A clean implementation and full analysis of the Insertion Sort algorithm — written as a companion to the source code for the Algorithms and Data Structures course.

---

## Algorithm Overview

Insertion Sort builds the sorted array one element at a time by taking each new element and inserting it into its correct position within the already-sorted region. It does not search for a global minimum — it simply places each element where it belongs relative to what's already sorted.

**Core idea in one sentence:** *Pick up the next element, shift larger elements right, drop it into the gap.*

---

## How It Works

Given an array of `n` elements, the algorithm performs `n - 1` passes, starting from index `1`.

On each pass `i`:
1. Store `arr[i]` as `key`
2. Scan leftward from `i - 1`, shifting each element one position right as long as it is greater than `key`
3. Insert `key` into the gap left by the shifting

The sorted region `arr[0..i-1]` grows by one element per pass.

### Trace Example

Input: `[12, 11, 13, 5, 6]`

```
Initial:   [12 | 11, 13, 5, 6]   sorted: [12]

Pass 1:  key=11  →  shift 12 right  →  insert 11  →  [11, 12 | 13, 5, 6]
Pass 2:  key=13  →  13 > 12? no     →  insert 13  →  [11, 12, 13 | 5, 6]
Pass 3:  key=5   →  shift 13,12,11  →  insert 5   →  [5, 11, 12, 13 | 6]
Pass 4:  key=6   →  shift 13,12,11  →  insert 6   →  [5, 6, 11, 12, 13]
Done.
```

### The Shift Mechanism (Pass 3 in detail)

`key = 5` is stored. A conceptual "hole" travels left as larger elements shift right:

```
[11, 12, 13, ?,  6]  ← hole at index 3 (key held separately)
[11, 12, ?,  13, 6]  ← 13 shifted right
[11, ?,  12, 13, 6]  ← 12 shifted right
[?,  11, 12, 13, 6]  ← 11 shifted right
[5,  11, 12, 13, 6]  ← key inserted at j+1 = 0
```

`key` is safe in a variable the entire time — nothing overwrites it.

---

## Implementation (Java)

```java
public static void insertionSort(int[] arr) {
    int n = arr.length;

    for (int i = 1; i < n; i++) {
        int key = arr[i];   // element to be inserted
        int j = i - 1;

        // shift elements greater than key one position to the right
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }

        arr[j + 1] = key;   // insert key into its correct position
    }
}
```

**Three details that matter:**

1. `key` is extracted before the loop — otherwise shifting would overwrite it.
2. The `while` condition uses strict `>` (not `>=`) — this is what makes the sort **stable**.
3. The final insert is at `j + 1`, not `j` — because `j` decrements one step past the insertion point before the loop exits.

---

## Loop Invariant

> At the start of each pass `i`, the subarray `arr[0..i-1]` contains the first `i` elements of the original array, rearranged in sorted (non-decreasing) order.

This invariant is subtly different from Selection Sort's. The sorted region contains *whatever was originally in those positions*, just now ordered — not necessarily the `i` globally smallest elements. Global correctness emerges only after the final pass.

**Proof sketch:**
- **Base case:** Before pass `1`, `arr[0..0]` contains one element — trivially sorted.
- **Inductive step:** If `arr[0..i-1]` is sorted, pass `i` inserts `arr[i]` into the correct position within it, producing a sorted `arr[0..i]`.
- **Termination:** After pass `n-1`, `arr[0..n-1]` is sorted — the full array.

---

## Complexity Analysis

### Time Complexity

The inner `while` loop runs a variable number of iterations depending on the input.

**Best case — already sorted:**
The condition `arr[j] > key` is false on the first check every pass. The inner loop never executes. Total comparisons: `n - 1`.

**Worst case — reverse sorted:**
Every element must be shifted past every element already in the sorted region.

```
Pass 1: 1 comparison
Pass 2: 2 comparisons
...
Pass n-1: n-1 comparisons

Total: 1 + 2 + ... + (n-1) = n(n-1)/2
```

**Average case:**
On average, each key is compared with half the sorted region. Total ≈ n²/4 comparisons and n²/4 shifts.

| Case | Comparisons | Shifts | Time |
|---|---|---|---|
| Best case (sorted) | n − 1 | 0 | **O(n)** |
| Average case | ~n²/4 | ~n²/4 | **O(n²)** |
| Worst case (reverse sorted) | n(n−1)/2 | n(n−1)/2 | **O(n²)** |

> **Critical insight:** The O(n) best case is real and exploited in practice. Timsort — used in Python and Java's `Arrays.sort()` for objects — uses Insertion Sort on small or nearly-sorted subarrays precisely because of this property.

### Space Complexity

Insertion Sort is **in-place**: only a single `key` variable and loop indices are needed as auxiliary storage.

| Space | Complexity |
|---|---|
| Auxiliary space | **O(1)** |
| Total space | **O(n)** (the input array) |

---

## Properties

| Property | Value | Notes |
|---|---|---|
| Stable | **Yes** | Strict `>` comparison preserves relative order of equal elements |
| Adaptive | **Yes** | Faster on partially sorted input — approaches O(n) |
| In-place | **Yes** | O(1) auxiliary space |
| Online | **Yes** | Can sort a stream; processes each element as it arrives |
| Comparison-based | **Yes** | Subject to O(n log n) lower bound for general sorting |

### Stability — Explained

Insertion Sort is **stable** because the inner `while` loop uses strict greater-than (`>`). Equal elements are never shifted past each other — the loop stops when it encounters an equal element and inserts the key immediately after it, preserving original relative order.

If the condition were changed to `>=`, the sort would become **unstable**: equal elements would be shifted unnecessarily, reversing their original order.

### Online Property — Explained

Insertion Sort is **online**: it can process elements one at a time as they arrive, always maintaining a sorted region of everything seen so far. You do not need to know the full input in advance.

Selection Sort cannot do this — it requires the full unsorted region to find the minimum each pass.

---

## Comparison with O(n²) Siblings

| Property | Insertion | Selection | Bubble |
|---|---|---|---|
| Best-case time | **O(n)** | O(n²) | O(n) |
| Worst-case time | O(n²) | O(n²) | O(n²) |
| Shifts / Swaps | O(n²) | **O(n) swaps** | O(n²) |
| Stable | **Yes** | No | Yes |
| Adaptive | **Yes** | No | Yes |
| Online | **Yes** | No | No |
| Preferred when | Nearly sorted input, small n, streaming data | Write cost >> read cost | Rarely preferred |

**Practical guideline:**
- Use Insertion Sort for small arrays (n ≤ ~20) or nearly-sorted data — the O(n) best case is a decisive advantage.
- Use Selection Sort when minimizing write operations matters (flash/EEPROM), since it makes at most n−1 swaps.
- Both are outperformed by O(n log n) algorithms (Merge Sort, Heap Sort, Quick Sort) for large, random input.

---

## Visualization of Passes

```
Index:     [0]   [1]   [2]   [3]   [4]
Initial:    12    11    13     5     6
            ^--- only index 0 is sorted

After P1:  [11    12] |  13     5     6     (11 inserted)
After P2:  [11    12    13] |   5     6     (13 already in place)
After P3:  [ 5    11    12    13] |   6     (5 shifted all the way left)
After P4:  [ 5     6    11    12    13]     (6 inserted, sort complete)
            └────────── sorted ──────────┘
```

Each `|` marks the boundary between the sorted and unsorted regions after that pass.

---

## Exam Trace Format

When writing an exam trace, show the key value held separately and the array after each insertion:

```
i=1: key=11   [12,  11, 13, 5, 6]  →  shift 12  →  [11, 12, 13, 5, 6]
i=2: key=13   [11,  12, 13, 5, 6]  →  no shift  →  [11, 12, 13, 5, 6]
i=3: key=5    [11,  12, 13, 5, 6]  →  shift 13,12,11  →  [5, 11, 12, 13, 6]
i=4: key=6    [5, 11, 12, 13, 6]   →  shift 13,12,11  →  [5, 6, 11, 12, 13]
```

Showing the `key` value explicitly and the shift steps earns full marks on most exam formats.

---

## References

- Cormen, T. H. et al. — *Introduction to Algorithms* (CLRS), 3rd ed., §2.1
- Sedgewick, R. — *Algorithms in Java*, Part 1–4, §2.1
- Knuth, D. E. — *The Art of Computer Programming*, Vol. 3: Sorting and Searching, §5.2.1
