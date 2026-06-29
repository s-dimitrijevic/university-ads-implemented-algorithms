public class Main {
    public static void main(String[] args) {

        QueueList queue = new QueueList();

        // --- isEmpty on new queue ---
        assert queue.isEmpty() : "FAIL: new queue should be empty";
        System.out.println("PASS: isEmpty on new queue");

        // --- size on new queue ---
        assert queue.size() == 0 : "FAIL: size should be 0";
        System.out.println("PASS: size on new queue");

        // --- enqueue ---
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assert queue.size() == 3 : "FAIL: size should be 3";
        assert !queue.isEmpty() : "FAIL: queue should not be empty";
        System.out.println("PASS: enqueue / size / isEmpty");

        // --- FIFO order ---
        assert queue.peek() == 1 : "FAIL: front should be 1";
        assert queue.peekEnd() == 3 : "FAIL: rear should be 3";
        System.out.println("PASS: FIFO order");

        // --- dequeue ---
        queue.dequeue();
        assert queue.peek() == 2 : "FAIL: front should be 2 after dequeue";
        assert queue.size() == 2 : "FAIL: size should be 2";
        System.out.println("PASS: dequeue");

        // --- dequeue until empty ---
        queue.dequeue();
        queue.dequeue();
        assert queue.isEmpty() : "FAIL: queue should be empty";
        System.out.println("PASS: dequeue until empty");

        // --- dequeue on empty throws ---
        try {
            queue.dequeue();
            System.out.println("FAIL: should have thrown on empty dequeue");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("PASS: dequeue on empty throws exception");
        }

        // --- peek on empty throws ---
        try {
            queue.peek();
            System.out.println("FAIL: should have thrown on empty peek");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("PASS: peek on empty throws exception");
        }

        // --- repeated cycle ---
        queue.enqueue(10);
        queue.dequeue();
        queue.enqueue(20);
        queue.enqueue(30);
        assert queue.peek() == 20 : "FAIL: front should be 20";
        assert queue.peekEnd() == 30 : "FAIL: rear should be 30";
        assert queue.size() == 2 : "FAIL: size should be 2";
        System.out.println("PASS: repeated enqueue/dequeue cycle");

        System.out.println("\nAll tests passed.");
    }
}
