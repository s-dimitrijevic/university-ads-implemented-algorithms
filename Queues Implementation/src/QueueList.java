public class QueueList {

    Node head;
    Node tail;
    int size;

    public int size(){
        return size;
    }

    public void enqueue(int data){

        //Adding to the rear - FIFO principles (first in, first out)
        Node newNode = new Node(data);

        if(size == 0){
            head = newNode;
            tail = newNode;
        }

        else{
            tail.next = newNode; //current queue end is linked to the new node
            tail = newNode; //new end is added to the list
        }

        size++;
    }

    public void dequeue(){

        if(size == 0) throw new IndexOutOfBoundsException("Queue is empty.");

        if(size == 1){
            tail = null;
            head = null;
        }

        else{
            head = head.next;
        }

        size--;
    }

    public int peek(){
        if(size == 0) throw new IndexOutOfBoundsException("Queue is empty.");
        return head.data;
    }

    public int peekEnd(){
        if(size == 0) throw new IndexOutOfBoundsException("Queue is empty.");
        return tail.data;
    }

    public boolean isEmpty(){
        return size == 0;
    }
}
