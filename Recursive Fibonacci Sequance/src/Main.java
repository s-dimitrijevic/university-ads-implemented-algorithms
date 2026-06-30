public class Main {
    static void main(String[] args){

        int f = 7;
        System.out.println(fibonacci(f));
    }

    public static int fibonacci(int f) {
        if(f == 1 || f == 0)
            return f;
        else
            return fibonacci(f-1) + fibonacci(f-2);
    }
}
