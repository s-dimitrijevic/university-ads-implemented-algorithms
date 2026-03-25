# Bubble Sort

A clean implementation and full analysis of the Bubble Sort algorithm — written as a companion to the source code for the Algorithms and Data Structures course.

---

## Algorithm Overview

Bubble Sort repeatedly walks through the array comparing adjacent pairs and swapping them if they are in the wrong order. Each pass "bubbles" the largest unsorted element to its correct position at the end. The process repeats until no swaps occur in a full pass — meaning the array is sorted.

**Core idea in one sentence:** *Compare neighbors, swap if wrong, repeat until nothing moves.*

---

## How It Works

Given an array of `n` elements, the algorithm performs at most `n - 1` passes.

On each pass:
1. Walk from index `0` to the last unsettled index
2. Compare each adjacent pair `arr[j]` and `arr[j+1]`
3. Swap them if `arr[j] > arr[j+1]`
4. After the pass, the largest unsettled element is guaranteed to be in its final position
5. If no swaps occurred during the pass, terminate early — the array is sorted

### Trace Example

Input: `[5, 3, 8, 4, 2]`

```
Pass 1:
  compare(5,3) → swap  → [3, 5, 8, 4, 2]
  compare(5,8) → ok    → [3, 5, 8, 4, 2]
  compare(8,4) → swap  → [3, 5, 4, 8, 2]
  compare(8,2) → swap  → [3, 5, 4, 2, 8]   ← 8 settled

Pass 2:
  compare(3,5) → ok    → [3, 5, 4, 2, 8]
  compare(5,4) → swap  → [3, 4, 5, 2, 8]
  compare(5,2) → swap  → [3, 4, 2, 5, 8]   ← 5 settled

Pass 3:
  compare(3,4) → ok    → [3, 4, 2, 5, 8]
  compare(4,2) → swap  → [3, 2, 4, 5, 8]   ← 4 settled

Pass 4:
  compare(3,2) → swap  → [2, 3, 4, 5, 8]   ← 3 settled, 2 trivially done
Done.
```

### The "Bubble" Mechanism

The maximum element gets carried rightward by every swap it triggers — it cannot stop until it reaches its correct position. This is the bubble rising:

```
Pass 1, carrying max element 8 rightward:

[5, 3, 8, 4, 2]
 ↑↑  compare 5,3 → swap
[3, 5, 8, 4, 2]
    ↑↑  compare 5,8 → ok (8 takes the lead)
[3, 5, 8, 4, 2]
       ↑↑  compare 8,4 → swap
[3, 5, 4, 8, 2]
          ↑↑  compare 8,2 → swap
[3, 5, 4, 2, 8]
                ↑ 8 settled ✓
```

---

## Implementation (Java)

### Basic version

```java
public static void bubbleSort(int[] arr) {
    int n = arr.length;
    for (int pass = 0; pass < n - 1; pass++) {
        for (int j = 0; j < n - 1 - pass; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}
```

### Optimized version — with early termination

```java
public static void bubbleSortOptimized(int[] arr) {
    int n = arr.length;
    for (int pass = 0; pass < n - 1; pass++) {
        boolean swapped = false;

        for (int j = 0; j < n - 1 - pass; j++) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
                swapped = true;
            }
        }

        if (!swapped) break;   // no swaps this pass → array is sorted
    }
}
```

**Three implementation details that matter:**

1. **Inner loop bound is `n - 1 - pass`**, not `n - 1` — skips the already-settled tail on each pass. Omitting `-pass` is a common exam mistake; the code still works but performs unnecessary comparisons.
2. **`swapped` flag** enables the O(n) best case. Without it, the algorithm always runs all `n - 1` passes even on a fully sorted array.
3. **Strict `>` in the comparison** — not `>=`. This is what makes the sort stable. Changing it to `>=` causes unnecessary swaps on equal elements and breaks stability.

---

## Loop Invariants

Two invariants operate simultaneously:

**Outer invariant:**
> After pass `k`, the subarray `arr[n-k..n-1]` contains the `k` largest elements in their final sorted positions. They will never be touched again.

**Inner invariant:**
> After comparing up to index `j` within a pass, the largest element seen so far in `arr[0..j+1]` is located at `arr[j+1]`.

The outer invariant is what guarantees forward progress — the settled zone grows by one element per pass. The inner invariant explains *why* the maximum always reaches the end: it gets carried rightward by every swap it triggers and cannot stop until it finds a larger neighbor or reaches the boundary.

**Proof of outer invariant by induction:**
- **Base case:** Before pass `0`, no elements are settled — trivially true.
- **Inductive step:** If the last `k` elements are settled after pass `k`, then pass `k+1` walks `arr[0..n-k-1]` and, by the inner invariant, places the maximum of that subarray at `arr[n-k-1]`. The settled zone now covers `k+1` elements.
- **Termination:** After `n-1` passes, `n-1` elements are settled. The remaining element is trivially in place.

---

## Complexity Analysis

### Time Complexity

The inner loop runs `n - 1 - pass` iterations on pass `pass`. Without early termination:

```
Pass 0: n-1 comparisons
Pass 1: n-2 comparisons
...
Pass n-2: 1 comparison

Total (worst): (n-1) + (n-2) + ... + 1 = n(n-1)/2
```

With the `swapped` flag, a fully sorted input triggers zero swaps on pass 0 and terminates immediately after `n-1` comparisons.

| Case | Comparisons | Swaps | Time |
|---|---|---|---|
| Best case (sorted, with flag) | n − 1 | 0 | **O(n)** |
| Best case (sorted, no flag) | n(n−1)/2 | 0 | **O(n²)** |
| Average case | ~n²/2 | ~n²/4 | **O(n²)** |
| Worst case (reverse sorted) | n(n−1)/2 | n(n−1)/2 | **O(n²)** |

> **Critical note:** The O(n) best case only applies to the optimized version with the `swapped` flag. The basic version is always O(n²) regardless of input.

### Space Complexity

Bubble Sort is **in-place**: only a `temp` variable, a `swapped` flag, and loop indices are used as auxiliary storage.

| Space | Complexity |
|---|---|
| Auxiliary space | **O(1)** |
| Total space | **O(n)** (the input array) |

---

## Properties

| Property | Value | Notes |
|---|---|---|
| Stable | **Yes** | Strict `>` prevents swapping equal elements |
| Adaptive | **Yes** (with flag) | Early termination on sorted input → O(n) |
| In-place | **Yes** | O(1) auxiliary space |
| Online | **No** | Cannot process streaming input |
| Practical use | **Rarely** | Worst constant factors of the three O(n²) sorts |

### Stability — Explained

Bubble Sort is **stable** because equal adjacent elements are never swapped. The condition `arr[j] > arr[j+1]` is false when the two values are equal, so the relative order of equal elements is always preserved.

### The Turtle Problem

A known weakness of Bubble Sort: large elements near the start move quickly to the right (one position per comparison within a pass), but small elements near the end move only one position to the left per *full pass*. These slow-moving small elements are called **turtles**.

This is why Insertion Sort tends to outperform Bubble Sort on nearly-sorted data in practice, even though both are adaptive and share the same O(n) best case. Insertion Sort places each element in one pass of shifting; Bubble Sort may require many passes to drag a turtle to the left.

**Cocktail Shaker Sort** (bidirectional Bubble Sort) mitigates this by alternating sweep direction each pass. Still O(n²) worst case, but handles turtles faster.

---

## Comparison with O(n²) Siblings

| Property | Bubble | Insertion | Selection |
|---|---|---|---|
| Best-case time | O(n) ¹ | **O(n)** | O(n²) |
| Worst-case time | O(n²) | O(n²) | O(n²) |
| Swaps | O(n²) | O(n²) | **O(n)** |
| Stable | **Yes** | **Yes** | No |
| Adaptive | **Yes** ¹ | **Yes** | No |
| Online | No | **Yes** | No |
| Practical use | Rarely | Small/nearly sorted data | Write-limited memory |

¹ Only with the `swapped` early-termination flag.

**Practical guideline:**
- Prefer Insertion Sort over Bubble Sort for nearly-sorted or small arrays — same O(n) best case, better constant factors, fewer total operations.
- Use Bubble Sort when simplicity and stability are required and input size is guaranteed small.
- Use Selection Sort when minimizing write operations matters (O(n) swaps).
- All three are outperformed by O(n log n) algorithms for large, random input.

---

## Visualization of Passes

```
Index:   [0]  [1]  [2]  [3]  [4]
Initial:   5    3    8    4    2
                                    settled: none

After P1:  3    5    4    2  [ 8]   settled: [4]
After P2:  3    4    2  [ 5    8]   settled: [3,4]
After P3:  3    2  [ 4    5    8]   settled: [2,3,4]
After P4:  2  [ 3    4    5    8]   settled: all
           └──────── sorted ───────┘
```

Each `[ ]` marks where the settled zone begins after that pass.

---

## Exam Trace Format

Show each comparison and swap explicitly within each pass:

```
Pass 1:  (5,3)→swap  (5,8)→ok  (8,4)→swap  (8,2)→swap  →  [3,5,4,2,8]
Pass 2:  (3,5)→ok   (5,4)→swap  (5,2)→swap              →  [3,4,2,5,8]
Pass 3:  (3,4)→ok   (4,2)→swap                          →  [3,2,4,5,8]
Pass 4:  (3,2)→swap                                      →  [2,3,4,5,8]
```

If the optimized version is used and an early termination occurs, explicitly note: *"No swaps in pass X — early termination."*

---

## References

- Cormen, T. H. et al. — *Introduction to Algorithms* (CLRS), 3rd ed., Problem 2-2
- Sedgewick, R. — *Algorithms in Java*, Part 1–4, §2.1
- Knuth, D. E. — *The Art of Computer Programming*, Vol. 3: Sorting and Searching, §5.2.2
