# Selection Sort

A clean implementation and full analysis of the Selection Sort algorithm — written as a companion to the source code for the Algorithms and Data Structures course.

---

## Algorithm Overview

Selection Sort works by repeatedly finding the minimum element in the unsorted portion of the array and placing it at the correct position. The boundary between the sorted and unsorted regions advances by one index per pass.

**Core idea in one sentence:** *Select the minimum, place it, repeat until done.*

---

## How It Works

Given an array of `n` elements, the algorithm performs `n - 1` passes.

On each pass `i` (starting from `0`):
1. Scan the subarray from index `i` to `n - 1`
2. Find the index of the minimum element in that range
3. Swap the minimum with the element at index `i`
4. Advance the sorted boundary to `i + 1`

### Trace Example

Input: `[64, 25, 12, 22, 11]`

```
Pass 1:  scan [64,25,12,22,11]  →  min=11 at idx 4  →  swap(0,4)  →  [11, 25, 12, 22, 64]
Pass 2:  scan [25,12,22,64]     →  min=12 at idx 2  →  swap(1,2)  →  [11, 12, 25, 22, 64]
Pass 3:  scan [25,22,64]        →  min=22 at idx 3  →  swap(2,3)  →  [11, 12, 22, 25, 64]
Pass 4:  scan [25,64]           →  min=25 at idx 3  →  no swap    →  [11, 12, 22, 25, 64]
Done.
```

---

## Implementation (Java)

```java
public static void selectionSort(int[] arr) {
    int n = arr.length;

    for (int i = 0; i < n - 1; i++) {
        // Find the minimum element in arr[i..n-1]
        int minIdx = i;
        for (int j = i + 1; j < n; j++) {
            if (arr[j] < arr[minIdx]) {
                minIdx = j;
            }
        }

        // Swap arr[i] with the found minimum
        int temp = arr[minIdx];
        arr[minIdx] = arr[i];
        arr[i] = temp;
    }
}
```

**Two loops:**
- Outer loop `i`: advances the sorted boundary
- Inner loop `j`: scans the unsorted region for the minimum
- One swap per outer iteration (at most)

---

## Loop Invariant

> At the start of each iteration `i`, the subarray `arr[0..i-1]` contains the `i` smallest elements of the original array, in sorted (non-decreasing) order.

This invariant holds because:
- **Base case:** Before pass `0`, the subarray `arr[0..-1]` is empty — trivially sorted.
- **Inductive step:** If `arr[0..i-1]` is correctly sorted after pass `i`, then pass `i+1` places the minimum of `arr[i..n-1]` at position `i`, extending the invariant.
- **Termination:** After pass `n-2`, `arr[0..n-2]` is sorted and contains the `n-1` smallest elements. The remaining element `arr[n-1]` is therefore the largest — the array is fully sorted.

---

## Complexity Analysis

### Time Complexity

The inner loop always runs `(n - i - 1)` iterations on pass `i`, regardless of the array's initial state.

Total comparisons:

```
(n-1) + (n-2) + (n-3) + ... + 1 = n(n-1)/2
```

This is always **Θ(n²)** — Selection Sort does not benefit from partial ordering.

| Case         | Comparisons | Swaps  | Time       |
|--------------|-------------|--------|------------|
| Best case    | n(n-1)/2    | O(n)   | **O(n²)**  |
| Average case | n(n-1)/2    | O(n)   | **O(n²)**  |
| Worst case   | n(n-1)/2    | O(n)   | **O(n²)**  |

> **Key distinction:** While comparisons are always Θ(n²), the number of *swaps* is at most `n - 1` — one per pass. This is a significant advantage over Bubble Sort (O(n²) swaps) when write operations are expensive.

### Space Complexity

Selection Sort is **in-place**: it uses only a constant amount of additional memory (`minIdx`, `temp`, loop indices).

| Space | Complexity |
|-------|------------|
| Auxiliary space | **O(1)** |
| Total space | **O(n)** (the input array) |

---

## Properties

| Property       | Value  | Notes                                                    |
|----------------|--------|----------------------------------------------------------|
| Stable         | **No** | Swapping non-adjacent elements can break relative order  |
| Adaptive       | **No** | Performs the same work on sorted and unsorted inputs     |
| In-place       | **Yes**| O(1) auxiliary space                                    |
| Comparison-based | **Yes** | Cannot beat O(n log n) lower bound for general input |
| Online         | **No** | Must see the entire unsorted region before each swap     |

### Stability — Explained

An algorithm is **stable** if equal elements maintain their original relative order.

Selection Sort is **not stable** because it performs long-range swaps. Consider:

```
Input: [3a, 3b, 1]   (3a and 3b are equal, a came first)

Pass 1: min=1 at idx 2 → swap(0,2) → [1, 3b, 3a]
                                            ↑   ↑
                                       3b now precedes 3a — order reversed
```

This matters when sorting objects by one field where other fields must preserve their original sequence (e.g., sorting a list of students by grade while preserving alphabetical order within each grade).

---

## Comparison with O(n²) Siblings

| Property         | Selection | Bubble    | Insertion |
|------------------|-----------|-----------|-----------|
| Best-case time   | O(n²)     | **O(n)**  | **O(n)**  |
| Worst-case time  | O(n²)     | O(n²)     | O(n²)     |
| Swaps            | **O(n)**  | O(n²)     | O(n²)     |
| Stable           | No        | Yes       | Yes       |
| Adaptive         | No        | Yes       | Yes       |
| Preferred when   | Writes are expensive | Nearly sorted (small n) | Nearly sorted (small n) |

**Practical guideline:** Use Selection Sort when write cost >> read cost (e.g., writing to EEPROM/flash, minimizing memory writes). For nearly-sorted data, Insertion Sort dominates. For general small inputs, all three perform similarly.

---

## When Selection Sort Is Actually Used

Despite its O(n²) complexity, Selection Sort has legitimate use cases:

1. **Write-limited memory** (EEPROM, flash storage): minimizes the number of write operations to O(n).
2. **Small arrays** (n ≤ ~20): constant factors favor simple algorithms over O(n log n) methods.
3. **Teaching**: the algorithm is maximally simple to reason about, implement, and prove correct.
4. **Embedded systems**: no recursion, no auxiliary arrays, predictable cache behavior.

---

## Visualization of Passes

```
Index:    [0]  [1]  [2]  [3]  [4]
Initial:   64   25   12   22   11
           ^----sorted boundary

After P1:  11 | 25   12   22   64     (11 placed)
After P2:  11   12 | 25   22   64     (12 placed)
After P3:  11   12   22 | 25   64     (22 placed)
After P4:  11   12   22   25 | 64     (25 placed, 64 trivially sorted)
           └─────── sorted ──────┘
```

Each `|` marks the sorted boundary after that pass.

---

## References

- Cormen, T. H. et al. — *Introduction to Algorithms* (CLRS), 3rd ed., §2.2
- Sedgewick, R. — *Algorithms in Java*, Part 1–4, §2.1
- Knuth, D. E. — *The Art of Computer Programming*, Vol. 3: Sorting and Searching, §5.2