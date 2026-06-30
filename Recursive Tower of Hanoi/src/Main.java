public class Main {

    static int moveCounter = 0;

    static void main(String[] args){


        hanoi_tower(6, 'A', 'B', 'C');
        System.out.println("Total moves: " +moveCounter);

    }

    static void hanoi_tower(int n, char A, char B, char C){

        if(n == 1)
            move(A,C);

        else{
            hanoi_tower(n-1, A, C, B);
            move(A,C);
            hanoi_tower(n-1, B, A, C);
        }
    }

    static void move(char A, char B){
        System.out.println("Moving from: "+A+" --> " +B);
        moveCounter++;
    }
}
