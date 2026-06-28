public class Main {
    public static void main(String[] args){

        //Singly Linked List
        SinglyLinkedList list = new SinglyLinkedList();

        // insertEnd
        list.insertEnd(10);
        list.insertEnd(20);
        list.insertEnd(30);
        System.out.println(list.show());        // [10-->20-->30]

        // insertStart
        list.insertStart(5);
        System.out.println(list.show());        // [5-->10-->20-->30]

        // getFirst / getLast
        System.out.println(list.getFirst());    // 5
        System.out.println(list.getLast());     // 30

        // search
        System.out.println(list.search(20));    // Found. 3
        System.out.println(list.search(99));    // -1

        // deleteStart
        list.deleteStart();
        System.out.println(list.show());        // [10-->20-->30]

        // deleteEnd
        list.deleteEnd();
        System.out.println(list.show());        // [10-->20]

        // size / isEmpty
        System.out.println(list.size());        // 2
        System.out.println(list.isEmpty());     // false

        // edge case: delete down to empty
        list.deleteEnd();
        list.deleteEnd();
        System.out.println(list.show());        // []
        System.out.println(list.isEmpty());     // true
        System.out.println(list.size());        // 0

        // edge case: insertStart on empty
        list.insertStart(42);
        System.out.println(list.show());        // [42]
        System.out.println(list.getFirst());    // 42
        System.out.println(list.getLast());     // 42
    }
}
