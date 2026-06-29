public class Main {
    static void main(String[] args) {

        int x = 23612;
        int y = 19284;

        int result = euclidGCD(x,y);
        System.out.println("GCD for "+x+ " and " +y+ " is "+result);
    }

    public static int euclidGCD(int x, int y){
        if(x % y == 0)
            return y;
        return euclidGCD(y, x % y);
    }
}
