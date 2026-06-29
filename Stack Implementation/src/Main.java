public class Main {

    public static void main(String[] args) {

        // ── 1. EMPTY STACK GUARDS ──────────────────────────────────────────
        Stack s = new Stack();

        try { s.pop(); System.out.println("FAIL - should have thrown"); }
        catch (IndexOutOfBoundsException e) { System.out.println("PASS - pop() on empty"); }

        try { s.peekTop(); System.out.println("FAIL - should have thrown"); }
        catch (IndexOutOfBoundsException e) { System.out.println("PASS - peekTop() on empty"); }

        try { s.peekBottom(); System.out.println("FAIL - should have thrown"); }
        catch (IndexOutOfBoundsException e) { System.out.println("PASS - peekBottom() on empty"); }

        try { s.size(); System.out.println("FAIL - should have thrown"); }
        catch (IndexOutOfBoundsException e) { System.out.println("PASS - size() on empty"); }

        // ── 2. SINGLE ELEMENT ──────────────────────────────────────────────
        s.put(10);
        System.out.println(s.peekTop()    == 10   ? "PASS - peekTop()" : "FAIL");
        System.out.println(s.peekBottom() == 10   ? "PASS - peekBottom()" : "FAIL");
        System.out.println(s.size()       == 1    ? "PASS - size() == 1" : "FAIL");

        // pop last element — top AND bottom must become null
        int val = s.pop();
        System.out.println(val            == 10   ? "PASS - pop() returned 10" : "FAIL");
        System.out.println(s.size         == 0    ? "PASS - size == 0 after pop" : "FAIL");
        System.out.println(s.top          == null ? "PASS - top is null" : "FAIL");
        System.out.println(s.bottom       == null ? "PASS - bottom is null" : "FAIL");

        // ── 3. MULTIPLE ELEMENTS ───────────────────────────────────────────
        s.put(1);
        s.put(2);
        s.put(3);  // top → 3, bottom → 1

        System.out.println(s.peekTop()    == 3 ? "PASS - peekTop() == 3" : "FAIL");
        System.out.println(s.peekBottom() == 1 ? "PASS - peekBottom() == 1" : "FAIL");
        System.out.println(s.size()       == 3 ? "PASS - size() == 3" : "FAIL");

        // ── 4. LIFO ORDER ──────────────────────────────────────────────────
        System.out.println(s.pop() == 3 ? "PASS - LIFO pop 3" : "FAIL");
        System.out.println(s.pop() == 2 ? "PASS - LIFO pop 2" : "FAIL");
        System.out.println(s.pop() == 1 ? "PASS - LIFO pop 1" : "FAIL");
        System.out.println(s.size == 0  ? "PASS - empty after full drain" : "FAIL");

        // ── 5. SHOW ────────────────────────────────────────────────────────
        System.out.println("\n-- show() on empty --");
        s.show();  // should print "Empty stack." and not crash

        s.put(10);
        s.put(20);
        s.put(30);
        System.out.println("\n-- show() expected: 30, 20, 10 top→bottom --");
        s.show();

        // ── 6. RE-USE AFTER FULL DRAIN ─────────────────────────────────────
        s.pop(); s.pop(); s.pop();
        s.put(99);
        System.out.println(s.peekTop()    == 99 ? "PASS - re-use after drain" : "FAIL");
        System.out.println(s.peekBottom() == 99 ? "PASS - bottom correct after re-use" : "FAIL");
    }
}