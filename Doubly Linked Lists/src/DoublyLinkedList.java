public class DoublyLinkedList {

    Node head;
    Node tail;
    int size;

    public DoublyLinkedList(){}

    public int getSize(){

        if(size == 0) throw new IndexOutOfBoundsException("List is empty!");
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int getHead(){

        if(size == 0) throw new IndexOutOfBoundsException("List is empty!");
        return head.data;
    }

    public int getTail(){

        if(size == 0) throw new IndexOutOfBoundsException("List is empty!");
        return tail.data;
    }

    public int searchValue(int target){

        Node currentNode = head;
        int counter = 0;

        while(currentNode != null){

            if(target == currentNode.data){
                System.out.println("Found value!");
                return counter;
            }

            currentNode = currentNode.nextNode;
            counter++;
        }

        System.out.println("Value not found!");
        return -1;
    }

    public void createStart(int data){

        Node newNode = new Node(data);

        if(size == 0){
            head = newNode;
            tail = newNode;
        }

        else {
            newNode.prevNode = null;
            newNode.nextNode = head;
            head.prevNode = newNode;
            head = newNode;
        }

        size++;
    }

    public void createEnd(int data){

        Node newNode = new Node(data);

        if(size == 0){
            head = newNode;
            tail = newNode;
        }

        else{
            newNode.nextNode = null;
            newNode.prevNode = tail;
            tail.nextNode = newNode;
            tail = newNode;
        }

        size++;
    }

    public void deleteStart(){

        if(size == 0){ throw new IndexOutOfBoundsException("List is already empty!");}

        if(size == 1){
            head = null;
            tail = null;
        }

        else{
            head = head.nextNode;
            head.prevNode = null;
        }

        size--;
    }

    public void deleteEnd(){

        if(size == 0){ throw new IndexOutOfBoundsException("List is already empty!");}

        if(size == 1){
            head = null;
            tail = null;
        }

        else{

            tail = tail.prevNode;
            tail.nextNode = null;
        }

        size--;

    }

    public String show(){

        if(size == 0)
            return "[]";

        StringBuilder sb = new StringBuilder("[");
        Node currentNode = head;

        while(currentNode != null){
            sb.append(currentNode.data);

            if(currentNode.nextNode != null)
                sb.append("-->");

            currentNode = currentNode.nextNode;
        }

        return sb.append("]").toString();
    }
}
