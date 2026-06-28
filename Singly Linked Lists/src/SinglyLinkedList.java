public class SinglyLinkedList {

    private Node head;
    private Node tail;
    private int size;

    public SinglyLinkedList(){}

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void insertEnd(int data){

        Node newNode = new Node(data);

        if(size == 0){
            head = newNode;
        }

        else{
            tail.nextNode = newNode;
        }

        tail = newNode;
        size++;
    }

    public void insertStart(int data){

        Node newNode = new Node(data, head);

        if(size == 0){
            tail = newNode;
        }

        head = newNode;
        size++;
    }

    public void deleteEnd(){

        if(size == 0)
            return;

        if(size == 1){
            head=tail=null;
        }

        else {
            Node current = head;

            while (current.nextNode != tail) {
                current = current.nextNode;
            }

            current.nextNode = null;
            tail = current;
        }

        size--;
    }

    public void deleteStart(){

        if(size == 0) {return;}
        if(size == 1) {head=tail=null;}

        else{
            head = head.nextNode;
        }

        size--;
    }

    public int search(int target){

        Node current = head;
        int i = 1;

        while(current != null){
            if(current.data == target) {
                System.out.println("Found.");
                return i;
            }
            i++;
            current = current.nextNode;
        }

        return -1;
    }

    public int getFirst(){

        if(size == 0) throw new IndexOutOfBoundsException("List is empty!");
        return head.data;
    }

    public int getLast(){

        if(size == 0) throw new IndexOutOfBoundsException("List is empty!");
        return tail.data;
    }

    public String show(){

        StringBuilder sb = new StringBuilder("[");

        Node current = head;
        while(current != null){
            sb.append(current.data);
            if(current.nextNode != null) {sb.append("-->");}
            current = current.nextNode;
        }

        return sb.append("]").toString();
    }
}
