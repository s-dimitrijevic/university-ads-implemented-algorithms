# Gale-Shapley Algorithm (Stable Matching)

A clean implementation and full analysis of the Gale-Shapley algorithm — written as a companion to the source code for the Algorithms and Data Structures course.

---

## Problem Overview

The Stable Matching Problem asks: given n men and n women, each with a ranked preference list over all members of the opposite group, find a **stable matching** — a complete pairing such that no two unpaired individuals would both prefer each other over their current partners.

An **unstable pair** (blocking pair) is a man M and a woman W who are not matched to each other, but M prefers W over his current partner AND W prefers M over her current partner. A stable matching contains no blocking pairs.

Core idea in one sentence: Repeatedly let free proposers make offers down their preference list — responders tentatively accept or upgrade, never committing permanently until the algorithm terminates.

---

## Key Definitions

| Term              | Definition                                                                              |
|-------------------|-----------------------------------------------------------------------------------------|
| Stable matching   | A complete matching with no blocking pairs                                              |
| Blocking pair     | (M, W) not matched to each other, but each prefers the other over their current partner |
| Proposer-optimal  | Every proposer gets the best partner they can receive in any stable matching             |
| Receiver-pessimal | Every receiver gets the worst partner they must accept in any stable matching            |
| Deferred acceptance | Receivers hold offers tentatively, never making final decisions until the end          |

---

## Algorithm

The canonical form uses men as proposers and women as receivers. The roles are interchangeable — swapping them produces a women-optimal stable matching instead.

```
Initialize:
  All men free
  All women unmatched
  Each man's proposal pointer at index 0 (first preference)

While there exists a free man who has not proposed to all women:
  m = any such free man
  w = next woman on m's preference list (advance pointer)

  If w is free:
    w tentatively accepts m
    (m, w) become engaged

  Else w is currently engaged to m':
    If w prefers m over m':
      w upgrades — accepts m, breaks engagement with m'
      m' becomes free again
    Else:
      w rejects m
      m remains free

Return current matching
```

---

## Trace Example

**3 men, 3 women.**

Men's preferences:
```
m1: w1, w2, w3
m2: w1, w3, w2
m3: w2, w1, w3
```

Women's preferences:
```
w1: m2, m1, m3
w2: m1, m2, m3
w3: m1, m2, m3
```

**Round 1 — all men propose to first choice:**
```
m1 → w1: w1 is free → (m1, w1) engaged
m2 → w1: w1 is engaged to m1
         w1 prefers m2 over m1 → upgrade
         (m2, w1) engaged, m1 freed
m3 → w2: w2 is free → (m3, w2) engaged
```

State: `{(m2,w1), (m3,w2)}` — m1 is free.

**Round 2 — m1 proposes to next on his list:**
```
m1 → w2: w2 is engaged to m3
         w2 prefers m1 over m3 → upgrade
         (m1, w2) engaged, m3 freed
```

State: `{(m2,w1), (m1,w2)}` — m3 is free.

**Round 3 — m3 proposes to next on his list:**
```
m3 → w1: w1 is engaged to m2
         w1 prefers m2 over m3 → reject
         m3 remains free
```

**Round 4 — m3 continues down his list:**
```
m3 → w3: w3 is free → (m3, w3) engaged
```

**Final matching: `{(m1,w2), (m2,w1), (m3,w3)}`**

No blocking pair exists — the matching is stable.

---

## Implementation (Java)

### Ranking Matrix

Checking if woman w prefers man m over man m2 naively requires scanning her preference list — O(n). Precomputing a ranking matrix converts this to O(1).

`womenRank[w][m]` = position of man m in woman w's preference list.
Lower value = higher preference.

`w prefers m over m2` ↔ `womenRank[w][m] < womenRank[w][m2]`

```java
public class GaleShapley {

    private final int n;
    private final int[][] menPref;    // menPref[m][i] = i-th preferred woman of man m
    private final int[][] womenRank;  // womenRank[w][m] = rank of man m in woman w's list

    /**
     * @param menPref   n×n array: menPref[m] is man m's preference list (women indices)
     * @param womenPref n×n array: womenPref[w] is woman w's preference list (men indices)
     */
    public GaleShapley(int n, int[][] menPref, int[][] womenPref) {
        this.n = n;
        this.menPref = menPref;
        this.womenRank = buildRankMatrix(womenPref);
    }

    // Precompute rank matrix for O(1) preference lookup
    private int[][] buildRankMatrix(int[][] womenPref) {
        int[][] rank = new int[n][n];
        for (int w = 0; w < n; w++) {
            for (int r = 0; r < n; r++) {
                rank[w][womenPref[w][r]] = r;  // man womenPref[w][r] has rank r for woman w
            }
        }
        return rank;
    }

    /**
     * Runs Gale-Shapley and returns the stable matching.
     * @return womanPartner[] where womanPartner[w] = index of matched man
     */
    public int[] findStableMatching() {
        int[] womanPartner = new int[n];   // womanPartner[w] = m, or -1 if free
        int[] manPartner   = new int[n];   // manPartner[m]   = w, or -1 if free
        int[] nextProposal = new int[n];   // nextProposal[m] = index into menPref[m]

        Arrays.fill(womanPartner, -1);
        Arrays.fill(manPartner, -1);

        int freeMenCount = n;

        while (freeMenCount > 0) {

            // Find any free man
            int m = -1;
            for (int i = 0; i < n; i++) {
                if (manPartner[i] == -1) { m = i; break; }
            }

            // Propose to next woman on his list
            int w = menPref[m][nextProposal[m]++];

            if (womanPartner[w] == -1) {
                // w is free — accept
                womanPartner[w] = m;
                manPartner[m]   = w;
                freeMenCount--;

            } else {
                int m2 = womanPartner[w];  // w's current partner

                if (womenRank[w][m] < womenRank[w][m2]) {
                    // w prefers m over m2 — upgrade
                    womanPartner[w] = m;
                    manPartner[m]   = w;
                    manPartner[m2]  = -1;   // m2 is now free
                    // freeMenCount unchanged: m consumed, m2 freed — net zero

                }
                // else: w rejects m — m stays free, nothing changes
            }
        }

        return womanPartner;
    }
}
```

### Usage Example

```java
int n = 3;

// m0 prefers w1, w0, w2 (in order)
int[][] menPref = {
    {1, 0, 2},  // m0
    {0, 2, 1},  // m1
    {0, 1, 2}   // m2
};

// w0 prefers m1, m0, m2 (in order)
int[][] womenPref = {
    {1, 0, 2},  // w0
    {0, 1, 2},  // w1
    {0, 1, 2}   // w2
};

GaleShapley gs = new GaleShapley(n, menPref, womenPref);
int[] matching = gs.findStableMatching();

for (int w = 0; w < n; w++) {
    System.out.println("w" + w + " ↔ m" + matching[w]);
}
```

---

## Key Implementation Details

Four details that matter:

1. **Ranking matrix is mandatory for correctness and efficiency** — scanning preference lists naively degrades the preference check from O(1) to O(n), making the algorithm O(n³) instead of O(n²). Precompute it in the constructor.
2. **`nextProposal[m]` pointer advances, never resets** — each man proposes down his list exactly once per woman. A man who has been rejected and re-freed continues from where he left off. Resetting this pointer creates infinite loops.
3. **`freeMenCount` does not change on upgrade** — when woman w upgrades from m2 to m, m is consumed (no longer free) and m2 is freed. The count stays the same. Only a successful first-time acceptance decrements it.
4. **The algorithm terminates because proposals are monotone** — each man's `nextProposal` pointer only moves forward, and there are n² possible proposals total. Termination in at most n² iterations is guaranteed.

---

## Complexity Analysis

### Time Complexity

Each man proposes to each woman at most once. With n men and n women:

| Phase              | Cost                                  |
|--------------------|---------------------------------------|
| Build rank matrix  | O(n²)                                 |
| Main loop (total)  | O(n²) — at most n² proposals          |
| Per proposal       | O(1) — rank matrix gives O(1) lookup  |
| **Total**          | **O(n²)**                             |

### Space Complexity

| Structure         | Space  | Notes                               |
|-------------------|--------|-------------------------------------|
| `menPref`         | O(n²)  | n men × n preferences each          |
| `womenPref`       | O(n²)  | n women × n preferences each        |
| `womenRank`       | O(n²)  | Precomputed ranking matrix           |
| `womanPartner`    | O(n)   | Current matching for women           |
| `manPartner`      | O(n)   | Current matching for men             |
| `nextProposal`    | O(n)   | Proposal pointer per man             |
| **Total**         | **O(n²)** |                                  |

---

## Properties

| Property               | Value   | Notes                                                        |
|------------------------|---------|--------------------------------------------------------------|
| Always terminates      | Yes     | At most n² proposals, each made once                        |
| Produces stable match  | Yes     | Guaranteed — proved by contradiction on blocking pairs       |
| Proposer-optimal       | Yes     | Men get best possible partner across all stable matchings    |
| Receiver-pessimal      | Yes     | Women get worst possible partner across all stable matchings |
| Unique result          | No      | Multiple stable matchings may exist; this finds proposer-optimal |
| Recursive              | No      | Iterative — while loop, not recursive calls                  |

---

## Proof of Stability (Sketch)

Assume for contradiction that (m, w) is a blocking pair — m is matched to w' and w is matched to m', but m prefers w over w' and w prefers m over m'.

Since m prefers w over w', he proposed to w before w'. So at some point, m proposed to w. But w ended up with m', meaning either:
- w rejected m immediately (preferring m' at that moment), or
- w later upgraded from m to someone else she preferred more.

In both cases, w ended up with someone she prefers at least as much as m. But we assumed w prefers m over m' — contradiction.

Therefore no blocking pair can exist. The matching is stable. ∎

---

## Comparison: Men-Optimal vs Women-Optimal

| Run Mode          | Proposers | Receivers | Optimal for   | Pessimal for  |
|-------------------|-----------|-----------|---------------|---------------|
| Men propose       | Men       | Women     | Men           | Women         |
| Women propose     | Women     | Men       | Women         | Men           |

Both runs produce a valid stable matching. The two results bracket the space of all possible stable matchings.

---

## Exam Notes

- **The algorithm is iterative, not recursive** — while loop drives proposal rounds. There is no call stack depth concern.
- **Stability proof by contradiction** — assume a blocking pair exists and derive a contradiction from the proposal mechanics. This is the standard exam proof structure.
- **Proposer-optimality is a theorem, not an observation** — it can be proven that no proposer can do better in any stable matching, not just in the one Gale-Shapley returns.
- **Preference lists must be complete** — every man must rank every woman and vice versa. Incomplete preference lists break the termination and stability guarantees.
- **The rank matrix separates input representation from preference lookup** — confusing these two is the most common implementation mistake. The preference list says who is preferred; the rank matrix answers "does w prefer m over m2?" in O(1).
- **O(n²) is tight** — in the worst case (e.g. all men prefer the same woman), every man proposes to every woman exactly once, producing exactly n² proposals.
