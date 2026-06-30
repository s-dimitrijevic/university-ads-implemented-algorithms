public class Main {
    static void main() {

        int n = 6;
        int result = factorial(n);
        System.out.println("Factorial of "+n+" is: "+result);

    }

    private static int factorial(int n) {
        if(n == 1)
            return n;
        else
            return factorial(n-1) * n;
    }
}
