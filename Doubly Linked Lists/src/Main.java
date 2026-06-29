public class Main {

    public static void main(String[] args) {

        // ── 1. EMPTY LIST GUARDS ───────────────────────────────────────────
        DoublyLinkedList list = new DoublyLinkedList();

        System.out.println(list.isEmpty() ? "PASS - isEmpty() on new list" : "FAIL");

        try { list.getSize(); System.out.println("FAIL - should have thrown"); }
        catch (IndexOutOfBoundsException e) { System.out.println("PASS - getSize() on empty"); }

        try { list.getHead(); System.out.println("FAIL - should have thrown"); }
        catch (IndexOutOfBoundsException e) { System.out.println("PASS - getHead() on empty"); }

        try { list.getTail(); System.out.println("FAIL - should have thrown"); }
        catch (IndexOutOfBoundsException e) { System.out.println("PASS - getTail() on empty"); }

        try { list.deleteStart(); System.out.println("FAIL - should have thrown"); }
        catch (IndexOutOfBoundsException e) { System.out.println("PASS - deleteStart() on empty"); }

        try { list.deleteEnd(); System.out.println("FAIL - should have thrown"); }
        catch (IndexOutOfBoundsException e) { System.out.println("PASS - deleteEnd() on empty"); }

        System.out.println(list.show().equals("[]") ? "PASS - show() on empty" : "FAIL");

        // ── 2. SINGLE ELEMENT ──────────────────────────────────────────────
        list.createStart(10);

        System.out.println(list.getHead()    == 10 ? "PASS - getHead() == 10" : "FAIL");
        System.out.println(list.getTail()    == 10 ? "PASS - getTail() == 10" : "FAIL");
        System.out.println(list.getSize()    == 1  ? "PASS - size == 1" : "FAIL");
        System.out.println(!list.isEmpty()         ? "PASS - isEmpty() false" : "FAIL");
        System.out.println(list.show().equals("[10]") ? "PASS - show() single element" : "FAIL");

        // deleteStart on single — head AND tail must null
        list.deleteStart();
        System.out.println(list.head   == null ? "PASS - head null after deleteStart" : "FAIL");
        System.out.println(list.tail   == null ? "PASS - tail null after deleteStart" : "FAIL");
        System.out.println(list.size   == 0    ? "PASS - size 0 after deleteStart" : "FAIL");

        list.createEnd(10);
        list.deleteEnd();
        System.out.println(list.head   == null ? "PASS - head null after deleteEnd" : "FAIL");
        System.out.println(list.tail   == null ? "PASS - tail null after deleteEnd" : "FAIL");
        System.out.println(list.size   == 0    ? "PASS - size 0 after deleteEnd" : "FAIL");

        // ── 3. CREATE START — forward & backward chain ─────────────────────
        list.createStart(3);
        list.createStart(2);
        list.createStart(1);  // head → 1 ↔ 2 ↔ 3 ← tail

        System.out.println(list.getHead() == 1 ? "PASS - head == 1" : "FAIL");
        System.out.println(list.getTail() == 3 ? "PASS - tail == 3" : "FAIL");
        System.out.println(list.getSize() == 3 ? "PASS - size == 3" : "FAIL");

        // forward chain
        System.out.println(list.head.nextNode.data              == 2 ? "PASS - forward 1→2" : "FAIL");
        System.out.println(list.head.nextNode.nextNode.data     == 3 ? "PASS - forward 2→3" : "FAIL");

        // backward chain
        System.out.println(list.tail.prevNode.data              == 2 ? "PASS - backward 3→2" : "FAIL");
        System.out.println(list.tail.prevNode.prevNode.data     == 1 ? "PASS - backward 2→1" : "FAIL");

        // boundary links
        System.out.println(list.head.prevNode              == null ? "PASS - head.prev null" : "FAIL");
        System.out.println(list.tail.nextNode              == null ? "PASS - tail.next null" : "FAIL");

        System.out.println(list.show().equals("[1-->2-->3]") ? "PASS - show() 1→2→3" : "FAIL");

        // ── 4. CREATE END — forward & backward chain ───────────────────────
        DoublyLinkedList list2 = new DoublyLinkedList();
        list2.createEnd(1);
        list2.createEnd(2);
        list2.createEnd(3);  // head → 1 ↔ 2 ↔ 3 ← tail

        System.out.println(list2.getHead() == 1 ? "PASS - head == 1" : "FAIL");
        System.out.println(list2.getTail() == 3 ? "PASS - tail == 3" : "FAIL");

        System.out.println(list2.head.nextNode.data          == 2 ? "PASS - forward 1→2" : "FAIL");
        System.out.println(list2.tail.prevNode.data          == 2 ? "PASS - backward 3→2" : "FAIL");
        System.out.println(list2.head.prevNode          == null ? "PASS - head.prev null" : "FAIL");
        System.out.println(list2.tail.nextNode          == null ? "PASS - tail.next null" : "FAIL");

        // ── 5. DELETE START ────────────────────────────────────────────────
        // list is [1-->2-->3]
        list.deleteStart();  // → [2-->3]
        System.out.println(list.getHead()      == 2    ? "PASS - head == 2 after deleteStart" : "FAIL");
        System.out.println(list.head.prevNode  == null ? "PASS - new head.prev null" : "FAIL");
        System.out.println(list.getSize()      == 2    ? "PASS - size == 2" : "FAIL");

        // ── 6. DELETE END ──────────────────────────────────────────────────
        // list is [2-->3]
        list.deleteEnd();  // → [2]
        System.out.println(list.getTail()      == 2    ? "PASS - tail == 2 after deleteEnd" : "FAIL");
        System.out.println(list.tail.nextNode  == null ? "PASS - new tail.next null" : "FAIL");
        System.out.println(list.getSize()      == 1    ? "PASS - size == 1" : "FAIL");

        // ── 7. SEARCH ──────────────────────────────────────────────────────
        DoublyLinkedList list3 = new DoublyLinkedList();
        list3.createEnd(10);
        list3.createEnd(20);
        list3.createEnd(30);  // [10-->20-->30]

        System.out.println(list3.searchValue(10) == 0  ? "PASS - found 10 at index 0" : "FAIL");
        System.out.println(list3.searchValue(20) == 1  ? "PASS - found 20 at index 1" : "FAIL");
        System.out.println(list3.searchValue(30) == 2  ? "PASS - found 30 at index 2" : "FAIL");
        System.out.println(list3.searchValue(99) == -1 ? "PASS - 99 not found" : "FAIL");

        // ── 8. SHOW ────────────────────────────────────────────────────────
        System.out.println(list3.show().equals("[10-->20-->30]") ? "PASS - show() correct" : "FAIL");

        // ── 9. RE-USE AFTER FULL DRAIN ─────────────────────────────────────
        list3.deleteStart();
        list3.deleteStart();
        list3.deleteStart();

        System.out.println(list3.isEmpty()              ? "PASS - empty after full drain" : "FAIL");
        System.out.println(list3.head        == null    ? "PASS - head null after drain" : "FAIL");
        System.out.println(list3.tail        == null    ? "PASS - tail null after drain" : "FAIL");

        list3.createEnd(99);
        System.out.println(list3.getHead()   == 99 ? "PASS - re-use after drain head" : "FAIL");
        System.out.println(list3.getTail()   == 99 ? "PASS - re-use after drain tail" : "FAIL");
        System.out.println(list3.getSize()   == 1  ? "PASS - size 1 after re-use" : "FAIL");
    }
}