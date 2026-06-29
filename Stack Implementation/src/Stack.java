public class Stack {

    Node top;
    Node bottom;
    int size;

    public Stack() {}

    public int size() {

        if (size == 0) {
            throw new IndexOutOfBoundsException("Stack is empty.");
        }

        return size;
    }

    public int peekTop() {

        if (size == 0) {
            throw new IndexOutOfBoundsException("Stack is empty.");
        }

        return top.data;
    }

    public int peekBottom() {

        if (size == 0) {
            throw new IndexOutOfBoundsException("Stack is empty.");
        }

        return bottom.data;
    }

    public void put (int data) {

        Node newNode = new Node(data);

        if(size == 0){
            top = newNode;
            bottom = newNode;
        }

        else{
            newNode.prev = top;
            top = newNode;
        }

        size++;
    }

    public int pop() {



        if (size == 0) {
            throw new IndexOutOfBoundsException("Stack is empty.");
        }

        int topData = top.data;

        if(size == 1){
            top = null;
            bottom = null;
        }

        else{
            top = top.prev;
        }

        size--;
        return topData;
    }

    public void show() {

        if(size == 0)
            System.out.println("Empty stack.");

        Node current = top;

        while(current != null){
            System.out.println("=== "+current.data+" ===");
            current = current.prev;
        }

    }
}
